package com.mycila.testing.plugins.jetty.config;

import com.mycila.testing.plugins.jetty.NopServerLifeCycleListener;
import com.mycila.testing.plugins.jetty.ServerLifeCycleListener;

public interface RawConfig {

    /**
     * The path of the WAR file to load. The search is based on the current directory.
     * <p>
     * Default is {@link DefaultConfig#DEFAULT_WAR_LOCATION}. If there is more than one WAR file the test will fail, else
     * the WAR file will be loaded and the test run.
     * <p>
     * WAR locator strategy :
     * <ul>
     * <li>default path is either relative or absolute
     * <li>starts with "reg:" to enable the following to be java regular expression for this path
     * <ul>
     * <li>the regular expression should starts with the current directory {@code './'}, which translated in java
     * pattern is {@code '\\.\\/'}
     * </ul>
     * <li>starts with "ant:" to enable the following to be ant path expression for this path, ie:
     * &#42;&#42;/webapp-*.war
     * <ul>
     * <li>? matches one character
     * <li>* matches zero or more characters
     * <li>** matches zero or more 'directories' in a path
     * </ul>
     * <li>starts with "sys:" to enable the following to be system property expression for this path
     * </ul>
     * 
     * @return path of the WAR file to load.
     */
    String getWarLocation();


    /**
     * The web application server port. Default is {@link DefaultConfig#DEFAULT_SERVER_PORT}.
     * 
     * @return the web application server port.
     */
    int getServerPort();


    /**
     * The web application context path. Must starts with a slash '/' but doesn't end with one
     * except for root context path. Default is {@link DefaultConfig#DEFAULT_CONTEXT_PATH}.
     * 
     * @return the web application context path.
     */
    String getContextPath();


    /**
     * True to start a new server (and stop the old one), false to start a server only if there is no running
     * one. If {@code true} then {@link #isDeployWebapp()} is logically force to true.
     * 
     * @return true to start a new server (and stop the old one), false to start a server only if there is no running
     *         one, default {@value DefaultConfig#DEFAULT_START_SERVER}.
     */
    boolean isStartServer();


    /**
     * True to deploy a new webapp (and undeploy the old one), false to deploy a webapp only if there is no
     * deployed one.
     * 
     * @return true to deploy a new webapp (and undeploy the old one), false to deploy a webapp only if there is no
     *         deployed one, default {@value DefaultConfig#DEFAULT_DEPLOY_WEBAPP}.
     */
    boolean isDeployWebapp();


    /**
     * True to skip starting server or deploying webapp. Default is {@link DefaultConfig#DEFAULT_SKIP}.
     * 
     * @return if skip starting server or deploying webapp.
     */
    boolean isSkip();


    /**
     * The server lifecycle listener which allow customization of the server configuration. Default is
     * {@link NopServerLifeCycleListener}, default {@value DefaultConfig#DEFAULT_CYCLE_LISTENER_CLASS}.
     * 
     * @return the server lifecycle listener.
     */
    Class<? extends ServerLifeCycleListener> getServerLifeCycleListenerClass();

}
