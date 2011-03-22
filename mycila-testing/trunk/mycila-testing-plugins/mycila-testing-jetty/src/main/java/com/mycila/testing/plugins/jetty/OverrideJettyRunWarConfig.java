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

import java.net.URL;

public class OverrideJettyRunWarConfig
        extends AbstractDefaultJettyRunWarConfig<JettyRunWar> {

    public OverrideJettyRunWarConfig(
            final JettyRunWarConfig config)
    {
        this.overrideConfig = new AnnotationJettyRunWarConfig();
        this.config = config;
    }


    public void init(
            final JettyRunWar data)
    {
        this.overrideConfig.init(data);
        this.override = data;
    }


    public URL getWarLocation()
    {
        return ((this.override.value() != DEFAULT_WAR_LOCATION) || !this.override.war().isEmpty())
                ? this.overrideConfig.getWarLocation()
                : this.config.getWarLocation();
    }


    public int getServerPort()
    {
        return (this.override.serverPort() != DEFAULT_SERVER_PORT)
                ? this.overrideConfig.getServerPort()
                : this.config.getServerPort();
    }


    public String getContextPath()
    {
        return (this.override.contextPath() != DEFAULT_CONTEXT_PATH)
                ? this.overrideConfig.getContextPath()
                : this.config.getContextPath();
    }


    public ServerLifeCycleListener getServerLifeCycleListener()
    {
        return (this.override.serverLifeCycleListener() != NopServerLifeCycleListener.class)
                ? this.overrideConfig.getServerLifeCycleListener()
                : this.config.getServerLifeCycleListener();
    }


    public boolean isDoStartServer()
    {
        return (this.override.startServer() != DEFAULT_DO_START_SERVER)
                ? this.overrideConfig.isDoStartServer()
                : this.config.isDoStartServer();
    }


    public boolean isDoDeployWebapp()
    {
        return (this.override.deployWebapp() != DEFAULT_DO_DEPLOY_WEBAPP)
                ? this.overrideConfig.isDoDeployWebapp()
                : this.config.isDoDeployWebapp();
    }


    public boolean isSkip()
    {
        return (this.override.skip() != DEFAULT_SKIP)
                ? this.overrideConfig.isSkip()
                : this.config.isSkip();
    }

    private final JettyRunWarConfig config;

    private JettyRunWar override;

    private final AnnotationJettyRunWarConfig overrideConfig;

}
