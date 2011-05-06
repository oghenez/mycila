package com.mycila.testing.plugins.jetty.config;

import java.lang.reflect.Method;
import java.net.URL;

import com.mycila.testing.plugins.jetty.JettyRunWar;
import com.mycila.testing.plugins.jetty.ServerLifeCycleListener;

public interface Config
        extends RawConfig {

    /**
     * The location of the WAR file to load.
     * 
     * @return the location of the WAR file to load.
     */
    URL getWarLocationUrl();


    /**
     * The server lifecycle listener which allow customization of the server configuration.
     * 
     * @return the server lifecycle listener.
     */
    ServerLifeCycleListener getServerLifeCycleListener();


    Class<?> getSourceClass();


    Method getSourceMethod();


    JettyRunWar getSourceConfig();

}
