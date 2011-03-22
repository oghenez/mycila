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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycila.testing.core.api.TestContext;
import com.mycila.testing.core.api.TestExecution;
import com.mycila.testing.core.plugin.DefaultTestPlugin;

/**
 * Testing plugin which load the web application specified by {@link JettyRunWar} annotation using jetty.
 * 
 * TODO ? rename package to com.mycila.testing.plugin.jetty
 * TODO use mycila logger
 * 
 * @author amertum
 */
public class JettyTestPlugin
        extends DefaultTestPlugin {

    /**
     * @see com.mycila.testing.core.plugin.DefaultTestPlugin#beforeTest(com.mycila.testing.core.api.TestExecution)
     */
    @Override
    public void beforeTest(
            final TestExecution testExecution)
        throws Exception
    {
        final JettyRunWarConfig<JettyRunWar> config;
        {
            final JettyRunWar runWar = this.getJettyRunWar(testExecution);
            this.logger.info("@" + JettyRunWar.class + " configuration : " + runWar);

            if (runWar == null) {
                this.logger.debug("skip " + JettyTestPlugin.class + " because there is no " + JettyRunWar.class
                        + " annotation on test class");
                return;
            }

            config = this.getJettyRunWarConfig(runWar);
            this.logger.info("jetty-config : " + config);
        }

        if (config.isSkip()) {
            this.logger.debug("skip running webapp with Jetty");
            return;
        }

        if (this.actions.hasWebAppContext() && config.isDoDeployWebapp()) {
            this.actions.stopWebapp();
        }
        if (this.actions.hasServer() && config.isDoStartServer()) {
            this.actions.stopServer();
        }

        if (!this.actions.hasServer()) {
            this.actions.createServer(testExecution, config);
            this.actions.startServer(testExecution, config);
        }
        else {
            this.logger.info("start server ? keep server running");
        }

        if (!this.actions.hasWebAppContext()) {
            this.actions.createWebAppContext(testExecution, config);
            this.actions.startWebApp();
        }
        else {
            this.logger.info("start webapp ? keep webapp running");
        }
    }


    /**
     * @see com.mycila.testing.core.plugin.DefaultTestPlugin#afterTest(com.mycila.testing.core.api.TestExecution)
     */
    @Override
    public void afterTest(
            final TestExecution testExecution)
        throws Exception
    {
        final JettyRunWar runWar = this.getJettyRunWar(testExecution);
        if (runWar == null) {
            this.logger.debug("skip " + JettyTestPlugin.class + " because there is no " + JettyRunWar.class
                    + " annotation on test class");
            return;
        }

        final JettyRunWarConfig<JettyRunWar> config = this.getJettyRunWarConfig(runWar);
        this.logger.info("jetty-config : " + config);

        if (config.isSkip()) {
            this.logger.debug("skip stopping webapp");
            return;
        }

        if (this.actions.hasWebAppContext() && config.isDoDeployWebapp()) {
            this.actions.stopWebapp();
        }
        else {
            this.logger.info("stop webapp ? keep webapp running");
        }

        if (this.actions.hasServer() && config.isDoStartServer()) {
            this.actions.stopServer();
        }
        else {
            this.logger.info("stop server ? keep server running");
        }
    }


    @Override
    public void shutdown(
            final TestContext context)
        throws Exception
    {
        this.logger.info("JVM shutdown");

        if (this.actions.hasWebAppContext()) {
            this.actions.stopWebapp();
        }

        if (this.actions.hasServer()) {
            this.actions.stopServer();
        }
    }


    private JettyRunWar getJettyRunWar(
            final TestExecution testExecution)
    {
        final Class<?> testClass = testExecution.method().getDeclaringClass();
        if (!testClass.isAnnotationPresent(JettyRunWar.class)) {
            return null;
        }

        final JettyRunWar runWar = testClass.getAnnotation(JettyRunWar.class);

        return runWar;
    }


    private JettyRunWarConfig<JettyRunWar> getJettyRunWarConfig(
            final JettyRunWar runWar)
        throws InstantiationException, IllegalAccessException
    {
        final JettyRunWarConfig<JettyRunWar> config = new OverrideJettyRunWarConfig(runWar.config().newInstance());
        config.init(runWar);

        return config;
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ServerWebappActions actions = new ServerWebappActions();

}
