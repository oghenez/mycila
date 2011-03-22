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

public class DefaultJettyRunWarConfig
        extends AbstractDefaultJettyRunWarConfig {

    public void init(
            final Object data)
    {
        // nop
    }


    public URL getWarLocation()
    {
        return this.getWarLocation(DEFAULT_WAR_LOCATION);
    }


    public int getServerPort()
    {
        return DEFAULT_SERVER_PORT;
    }


    public String getContextPath()
    {
        return DEFAULT_CONTEXT_PATH;
    }


    public ServerLifeCycleListener getServerLifeCycleListener()
    {
        return this.getServerLifeCycleListener(DEFAULT_CYCLE_LISTENER_CLASS);
    }


    public boolean isDoStartServer()
    {
        return DEFAULT_DO_START_SERVER;
    }


    public boolean isDoDeployWebapp()
    {
        return DEFAULT_DO_DEPLOY_WEBAPP;
    }


    public boolean isSkip()
    {
        return DEFAULT_SKIP;
    }

}
