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

import static com.mycila.testing.plugins.jetty.config.DefaultConfig.hasJettyPlugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mycila.testing.core.api.TestContext;
import com.mycila.testing.core.api.TestExecution;
import com.mycila.testing.core.plugin.DefaultTestPlugin;
import com.mycila.testing.plugins.jetty.config.Config;
import com.mycila.testing.plugins.jetty.config.DefaultConfig;

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
        if (!hasJettyPlugin(testExecution.method())) {
            return;
        }

        final Config config = DefaultConfig.configFrom(testExecution.method());
        this.logger.info("jetty-config : " + config);

        if (config.isSkip()) {
            this.logger.debug("skip running webapp with Jetty");
            return;
        }

        if (this.actions.hasWebAppContext() && config.isDeployWebapp()) {
            this.actions.stopWebapp(config);
        }
        if (this.actions.hasServer() && config.isStartServer()) {
            this.actions.stopServer(config);
        }

        if (!this.actions.hasServer()) {
            this.actions.createServer(testExecution, config);
            this.actions.startServer(config);
        }
        else {
            this.logger.info("start server ? keep server running");
        }

        if (!this.actions.hasWebAppContext()) {
            this.actions.createWebAppContext(config);
            this.actions.startWebApp(config);
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
        if (!hasJettyPlugin(testExecution.method())) {
            this.logger.debug("skip " + JettyTestPlugin.class + " because there is no " + JettyRunWar.class
                    + " annotation on test class");
            return;
        }

        final Config config = DefaultConfig.configFrom(testExecution.method());
        this.logger.info("jetty-config : " + config);

        if (config.isSkip()) {
            this.logger.debug("skip stopping webapp");
            return;
        }

        if (this.actions.hasWebAppContext() && config.isDeployWebapp()) {
            this.actions.stopWebapp(config);
        }
        else {
            this.logger.info("stop webapp ? keep webapp running");
        }

        if (this.actions.hasServer() && config.isStartServer()) {
            this.actions.stopServer(config);
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
        this.logger.debug("JVM shutdown");

        if (this.actions.hasWebAppContext()) {
            this.actions.stopWebapp(null);
        }

        if (this.actions.hasServer()) {
            this.actions.stopServer(null);
        }
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ServerWebappActions actions = new ServerWebappActions();

}
