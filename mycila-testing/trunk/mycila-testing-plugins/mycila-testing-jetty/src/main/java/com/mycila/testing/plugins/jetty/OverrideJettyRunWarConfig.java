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
        return (!DEFAULT_WAR_LOCATION.equals(this.override.value()) || !this.override.war().isEmpty())
                ? this.overrideConfig.getWarLocation()
                : this.config.getWarLocation();
    }
    

    public int getServerPort()
    {
        return (DEFAULT_SERVER_PORT != this.override.serverPort())
                ? this.overrideConfig.getServerPort()
                : this.config.getServerPort();
    }
    

    public String getContextPath()
    {
        return !DEFAULT_CONTEXT_PATH.equals(this.override.contextPath())
                ? this.overrideConfig.getContextPath()
                : this.config.getContextPath();
    }
    

    public ServerLifeCycleListener getServerLifeCycleListener()
    {
        return !NopServerLifeCycleListener.class.equals(this.override.serverLifeCycleListener())
                ? this.overrideConfig.getServerLifeCycleListener()
                : this.config.getServerLifeCycleListener();
    }
    

    public boolean isDoStartServer()
    {
        return (DEFAULT_DO_START_SERVER != this.override.startServer())
                ? this.overrideConfig.isDoStartServer()
                : this.config.isDoStartServer();
    }
    

    public boolean isDoDeployWebapp()
    {
        return (DEFAULT_DO_DEPLOY_WEBAPP != this.override.deployWebapp())
                ? this.overrideConfig.isDoDeployWebapp()
                : this.config.isDoDeployWebapp();
    }
    

    public boolean isSkip()
    {
        return (DEFAULT_SKIP != this.override.skip())
                ? this.overrideConfig.isSkip()
                : this.config.isSkip();
    }
    

    private final JettyRunWarConfig config;
    
    private JettyRunWar override;
    
    private final AnnotationJettyRunWarConfig overrideConfig;
    
}
