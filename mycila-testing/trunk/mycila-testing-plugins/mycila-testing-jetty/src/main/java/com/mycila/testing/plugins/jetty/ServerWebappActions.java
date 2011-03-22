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
import java.net.URISyntaxException;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycila.testing.core.api.TestExecution;

public class ServerWebappActions {

    public void createServer(
            final TestExecution testExecution,
            final JettyRunWarConfig config)
    {
        this.server = new Server(config.getServerPort());
        this.server.addLifeCycleListener(new AbstractLifeCycle.AbstractLifeCycleListener() {

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
                ServerWebappActions.this.ready.set(true);
            }
        });

        final HandlerCollection handlerCollection = new HandlerCollection();
        this.contextHandlerCollection = new ContextHandlerCollection();
        handlerCollection.addHandler(this.contextHandlerCollection);
        this.server.setHandler(handlerCollection);
    }


    public void startServer(
            final TestExecution testExecution,
            final JettyRunWarConfig config)
        throws Exception
    {
        this.logger.info("server starting on localhost:{}", Integer.toString(config.getServerPort()));
        config.getServerLifeCycleListener().serverStarting(testExecution, this.server);

        this.server.start();
        final Callable<Boolean> isReady = new WaitUntilReadyCallable(this.ready);
        await().until(isReady, equalTo(true));

        config.getServerLifeCycleListener().serverStarted(testExecution, this.server);
        this.logger.info("server started");
    }


    public void stopServer()
        throws Exception
    {
        this.logger.info("server stopping");
        //config.getServerLifeCycleListener().serverStopping(testExecution, this.server);

        this.server.stop();
        this.server.destroy();

        //config.getServerLifeCycleListener().serverStopped(testExecution, this.server);
        this.logger.info("server stopped");

        this.server = null;
    }


    public boolean hasServer()
    {
        return (this.server != null);
    }


    public Server getServer()
    {
        return this.server;
    }


    public void createWebAppContext(
            final TestExecution testExecution,
            final JettyRunWarConfig config)
        throws URISyntaxException
    {
        final File warFile = new File(config.getWarLocation().toURI());
        if (!warFile.exists()) {
            throw new AssertionError("non-existent WAR : " + warFile.getAbsolutePath());
        }
        if (!"/".equals(config.getContextPath())
                && (!config.getContextPath().startsWith("/") || config.getContextPath().endsWith("/"))) {
            throw new AssertionError("contextPath must starts with a slash '/' but doesn't end with one");
        }

        this.webapp = new WebAppContext();
        //webapp.addLocaleEncoding("fr_FR", "UTF-8");
        this.webapp.setWar(config.getWarLocation().getFile());
        this.webapp.setContextPath(config.getContextPath());
        this.webapp.setCopyWebDir(false);
        this.webapp.setExtractWAR(false);
        this.webapp.setLogUrlOnStart(true);
        {
            final MimeTypes mimeTypes = new MimeTypes();
            mimeTypes.addMimeMapping("js", "application/javascript");
            this.webapp.setMimeTypes(mimeTypes);
        }
        //        if (this.webdefaultFile != null) {
        //            webapp.setDefaultsDescriptor(this.webdefaultFile.getAbsolutePath());
        //        }

        this.logger.info("webapp on localhost:{}{} with WAR:{}", new Object[] {
                Integer.valueOf(config.getServerPort()), config.getContextPath(), config.getWarLocation()
        });
    }


    public void startWebApp()
        throws Exception
    {
        this.logger.info("webapp starting");

        this.contextHandlerCollection.addHandler(this.webapp);
        this.webapp.start();

        this.logger.info("webapp started");
    }


    public void stopWebapp()
        throws Exception
    {
        this.logger.info("webapp stopping");

        this.webapp.stop();
        this.contextHandlerCollection.removeHandler(this.webapp);

        this.logger.info("webapp stopped");
        this.webapp = null;
    }


    public boolean hasWebAppContext()
    {
        return (this.webapp != null);
    }


    public WebAppContext getWebAppContext()
    {
        return this.webapp;
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final AtomicBoolean ready = new AtomicBoolean();

    private ContextHandlerCollection contextHandlerCollection;

    private Server server;

    private WebAppContext webapp;

    private static class WaitUntilReadyCallable
            implements Callable<Boolean> {

        public WaitUntilReadyCallable(
                final AtomicBoolean ready)
        {
            this.ready = ready;
        }


        public Boolean call()
            throws Exception
        {
            return this.ready.get();
        }

        private final AtomicBoolean ready;

    }

}
