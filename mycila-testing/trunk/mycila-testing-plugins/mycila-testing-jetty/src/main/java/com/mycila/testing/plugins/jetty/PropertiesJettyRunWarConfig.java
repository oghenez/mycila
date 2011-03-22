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
import java.util.Properties;

public class PropertiesJettyRunWarConfig
        extends AbstractDefaultJettyRunWarConfig<Properties> {

    public void init(
            final Properties data)
    {
        this.properties = data;
    }


    public URL getWarLocation()
    {
        final String value = this.properties.getProperty(PROPERTY_WAR_LOCATION, DEFAULT_WAR_LOCATION);
        final URL url = this.getWarLocation(value);

        return url;
    }


    public int getServerPort()
    {
        final String value = this.properties.getProperty(PROPERTY_SERVER_PORT, DEFAULT_SERVER_PORT_AS_STRING);
        final int intValue = Integer.parseInt(value);

        return intValue;
    }


    public String getContextPath()
    {
        final String value = this.properties.getProperty(PROPERTY_CONTEXT_PATH, DEFAULT_CONTEXT_PATH);

        return value;
    }


    public ServerLifeCycleListener getServerLifeCycleListener()
    {
        final String value = this.properties.getProperty(
                PROPERTY_CYCLE_LISTENER_CLASS,
                DEFAULT_CYCLE_LISTENER_CLASS_AS_STRING);
        final ServerLifeCycleListener listener = this.getServerLifeCycleListener(value);

        return listener;
    }


    public boolean isDoStartServer()
    {
        final String value = this.properties.getProperty(PROPERTY_DO_START_SERVER, DEFAULT_DO_START_SERVER_AS_STRING);
        final boolean boolValue = Boolean.parseBoolean(value);

        return boolValue;
    }


    public boolean isDoDeployWebapp()
    {
        final String value = this.properties.getProperty(PROPERTY_DO_DEPLOY_WEBAPP, DEFAULT_DO_DEPLOY_WEBAPP_AS_STRING);
        final boolean boolValue = Boolean.parseBoolean(value);

        return boolValue;
    }


    public boolean isSkip()
    {
        final String value = this.properties.getProperty(PROPERTY_SKIP, DEFAULT_SKIP_AS_STRING);
        final boolean boolValue = Boolean.parseBoolean(value);

        return boolValue;
    }

    private static final String PROPERTY_WAR_LOCATION = "warLocation";

    private static final String PROPERTY_SERVER_PORT = "serverPort";

    private static final String PROPERTY_CONTEXT_PATH = "contextPath";

    private static final String PROPERTY_CYCLE_LISTENER_CLASS = "cycleListenerClass";

    private static final String PROPERTY_DO_START_SERVER = "doStartServer";

    private static final String PROPERTY_DO_DEPLOY_WEBAPP = "doDeployWebapp";

    private static final String PROPERTY_SKIP = "skip";

    private Properties properties;

}
