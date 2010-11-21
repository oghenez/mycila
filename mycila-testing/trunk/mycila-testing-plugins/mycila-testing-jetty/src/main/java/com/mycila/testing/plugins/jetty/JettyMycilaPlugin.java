/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mycila.testing.plugins.jetty;

import static com.jayway.awaitility.Awaitility.await;
import static org.hamcrest.CoreMatchers.equalTo;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycila.testing.core.Mycila;
import com.mycila.testing.core.api.TestExecution;
import com.mycila.testing.core.plugin.DefaultTestPlugin;

/**
 * {@link Mycila} testing plugin which load the web application specified by {@link JettyRunWar} using jetty.
 * 
 * TODO rename to Jetty[RunWar]TestPlugin
 * TODO rename package to com.mycila.testing.plugin.jetty
 * TODO use mycila logger
 */
public class JettyMycilaPlugin
        extends DefaultTestPlugin {

    /**
     * @see com.mycila.testing.core.plugin.DefaultTestPlugin#beforeTest(com.mycila.testing.core.api.TestExecution)
     */
    @Override
    public void beforeTest(
            final TestExecution testExecution)
        throws Exception
    {
        final Class<?> testClass = testExecution.method().getDeclaringClass();
        if (!testClass.isAnnotationPresent(JettyRunWar.class)) {
            this.logger.debug("skip " + JettyMycilaPlugin.class + " because there is no " + JettyRunWar.class
                    + " annotation on test class");
            return;
        }

        final JettyRunWar runWar = testClass.getAnnotation(JettyRunWar.class);
        this.logger.info("@" + JettyRunWar.class + " configuration : " + runWar);

        // use default value if war not used
        final String warPath = (runWar.war().length() != 0)
                ? runWar.war()
                : runWar.value();
        // use fix fallback locator if warPath is unspecified
        final FileLocator fallbackFileLocator = (warPath.length() == 0)
                ? new FallbackFileLocator(this.fileLocator, new FixPathFileLocator(new RegFileLocator(), ".*\\.war"))
                : this.fileLocator;
        this.warFile = fallbackFileLocator.locate(warPath);
        if (!this.warFile.exists()) {
            throw new AssertionError("non-existent WAR : " + this.warFile.getAbsolutePath());
        }
        this.contextPath = runWar.contextPath();
        if (!"/".equals(this.contextPath) && (!this.contextPath.startsWith("/") || this.contextPath.endsWith("/"))) {
            throw new AssertionError("contextPath must starts with a slash '/' but doesn't end with one");
        }
        this.port = runWar.serverPort();

        this.logger.info("jetty starting on localhost:{}{} with WAR:{}", new Object[] {
                this.port, this.contextPath, this.warFile.getAbsolutePath()
        });
        final AtomicBoolean ready = new AtomicBoolean();

        this.server = makeServer(testExecution, this.warFile, this.port, this.contextPath, ready);
        this.server.start();

        final Callable<Boolean> isReady = new Callable<Boolean>() {

            @Override
            public Boolean call()
                throws Exception
            {
                return ready.get();
            }
        };
        await().until(isReady, equalTo(true));
        this.logger.info("jetty started");
    }


    /**
     * @see com.mycila.testing.core.plugin.DefaultTestPlugin#afterTest(com.mycila.testing.core.api.TestExecution)
     */
    @Override
    public void afterTest(
            final TestExecution testExecution)
        throws Exception
    {
        if (this.server != null) {
            this.logger.info("jetty stopping");

            this.server.stop();
            this.server.destroy();

            this.logger.info("jetty stopped");
        }
    }


    private static Server makeServer(
            final TestExecution testExecution,
            final File warFile,
            final int port,
            final String contextPath,
            final AtomicBoolean ready)
    {
        final WebAppContext webapp = new WebAppContext();
        //webapp.addLocaleEncoding("fr_FR", "UTF-8");
        webapp.setWar(warFile.getAbsolutePath());
        webapp.setContextPath(contextPath);
        webapp.setCopyWebDir(false);
        webapp.setExtractWAR(false);
        webapp.setLogUrlOnStart(true);
        {
            final MimeTypes mimeTypes = new MimeTypes();
            mimeTypes.addMimeMapping("js", "application/javascript");
            webapp.setMimeTypes(mimeTypes);
        }
        //        if (this.webdefaultFile != null) {
        //            webapp.setDefaultsDescriptor(this.webdefaultFile.getAbsolutePath());
        //        }

        final Server localServer = new Server(port);
        localServer.setHandler(webapp);

        localServer.addLifeCycleListener(new AbstractLifeCycle.AbstractLifeCycleListener() {

            @Override
            public void lifeCycleFailure(
                    final LifeCycle event,
                    final Throwable cause)
            {
                testExecution.setThrowable(cause);
            }


            @Override
            public void lifeCycleStarted(
                    final LifeCycle event)
            {
                ready.set(true);
            }
        });

        return localServer;
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FileLocator fileLocator = new StrategyFileLocator();

    private File warFile;

    private String contextPath;

    private int port;

    private Server server;

}
