package com.mycila.testing.plugins.jetty;

import java.net.URL;

/**
 * @param <DataType>
 *        the type of the data to initialize this configuration.
 */
public interface JettyRunWarConfig<DataType> {

    /**
     * Initialize this configuration.
     * 
     * @param data
     *        the data to initialize this configuration.
     */
    void init(
            DataType data);


    /**
     * The location of the WAR file to load.
     * 
     * @return the location of the WAR file to load.
     */
    URL getWarLocation();


    /**
     * The web application server port.
     * 
     * @return the web application server port.
     */
    int getServerPort();


    /**
     * The web application context path.
     * 
     * @return the web application context path.
     */
    String getContextPath();


    /**
     * The server lifecycle listener which allow customization of the server configuration.
     * 
     * @return the server lifecycle listener.
     */
    ServerLifeCycleListener getServerLifeCycleListener();


    /**
     * Returns true to to start a new server (and stop the old one), false to start a server only if there is no running
     * one. If {@code true} then {@link #isDoDeployWebapp()} is logically force to true.
     * 
     * @return true to to start a new server (and stop the old one), false to start a server only if there is no running
     *         one.
     */
    boolean isDoStartServer();


    /**
     * Returns true to to deploy a new webapp (and undeploy the old one), false to deploy a webapp only if there is no
     * deployed one.
     * 
     * @return true to to deploy a new webapp (and undeploy the old one), false to deploy a webapp only if there is no
     *         deployed one.
     */
    boolean isDoDeployWebapp();


    /**
     * Returns true to skip starting server or deploying webapp, false else.
     * 
     * @return true to skip starting server or deploying webapp, false else.
     */
    boolean isSkip();

}
