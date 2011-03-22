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

/**
 * {@code JettyRunWarConfig} implementation {@link JettyRunWar} annotation.
 */
public class AnnotationJettyRunWarConfig
        extends AbstractDefaultJettyRunWarConfig<JettyRunWar> {

    public void init(
            final JettyRunWar data)
    {
        this.jettyRunWar = data;
    }


    public URL getWarLocation()
    {
        // use default value if war not used
        final String warPath = this.jettyRunWar.value();
        return this.getWarLocation(warPath);
    }


    public int getServerPort()
    {
        return this.jettyRunWar.serverPort();
    }


    public String getContextPath()
    {
        return this.jettyRunWar.contextPath();
    }


    public ServerLifeCycleListener getServerLifeCycleListener()
    {
        return this.getServerLifeCycleListener(this.jettyRunWar.serverLifeCycleListener());
    }


    public boolean isDoStartServer()
    {
        return this.jettyRunWar.startServer();
    }


    public boolean isDoDeployWebapp()
    {
        return this.jettyRunWar.deployWebapp();
    }


    public boolean isSkip()
    {
        return this.jettyRunWar.skip();
    }

    private JettyRunWar jettyRunWar;

}
