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

package com.mycila.testing.plugins.jetty.config;

import com.mycila.testing.plugins.jetty.ServerLifeCycleListener;

/**
 * Set of configuration properties required to run the servlet container and deploy the web application.
 * {@link DefaultConfig} can be used as default base implementation.
 * 
 * @see DefaultConfig
 */
public interface RawConfig {

    /**
     * The location of the WAR file to load. The search is based on the current directory.
     * <p>
     * Default is {@value DefaultConfig#DEFAULT_WAR_LOCATION}. If there is more than one WAR file the test will fail,
     * else the WAR file will be loaded and the test run.
     * <p>
     * WAR locator strategy :
     * <ul>
     * <li>default path is either relative or absolute, ie: <code>'path/to/webapp.war'</code>;
     * <li>starts with "reg:" to enable the following to be java regular expression for this path, ie :
     * <code>'reg:\\.\\/webapp-.*\\.war'</code>;
     * <ul>
     * <li>the regular expression should starts with the current directory {@code './'}, which translated in java
     * pattern is {@code '\\.\\/'}
     * </ul>
     * <li>starts with "ant:" to enable the following to be ant path expression for this path, ie:
     * <code>'&#42;&#42;/webapp-*.war'</code>;
     * <ul>
     * <li>? matches one character;
     * <li>* matches zero or more characters;
     * <li>** matches zero or more 'directories' in a path.
     * </ul>
     * <li>starts with "sys:" to enable the following to be system property expression for this path, ie :
     * <code>'sys:warPath'</code>.
     * </ul>
     * 
     * @return location of the WAR file to load.
     */
    String getWarLocation();


    /**
     * The web application server port. Default is {@value DefaultConfig#DEFAULT_SERVER_PORT}.
     * 
     * @return the web application server port.
     */
    int getServerPort();


    /**
     * The web application context path. Must starts with a slash '/' but doesn't end with one
     * except for root context path. Default is {@value DefaultConfig#DEFAULT_CONTEXT_PATH}.
     * 
     * @return the web application context path.
     */
    String getContextPath();


    /**
     * True to start a new server (and stop the old one), false to start a server only if there is no running
     * one. If {@code true} then {@link #isDeployWebapp()} is logically force to {@code true}. Default
     * {@value DefaultConfig#DEFAULT_START_SERVER}.
     * 
     * @return true to start a new server (and stop the old one), false to start a server only if there is no running
     *         one.
     */
    boolean isStartServer();


    /**
     * True to deploy a new webapp (and undeploy the old one), false to deploy a webapp only if there is no
     * deployed one. Default {@value DefaultConfig#DEFAULT_DEPLOY_WEBAPP}
     * 
     * @return true to deploy a new webapp (and undeploy the old one), false to deploy a webapp only if there is no
     *         deployed one.
     */
    boolean isDeployWebapp();


    /**
     * True to skip starting server or deploying webapp. Default is {@value DefaultConfig#DEFAULT_SKIP}.
     * 
     * @return true to skip starting server or deploying webapp.
     */
    boolean isSkip();


    /**
     * The {@link ServerLifeCycleListener} class which allow customization of the server configuration. Default is
     * {@value DefaultConfig#DEFAULT_CYCLE_LISTENER_CLASS}.
     * 
     * @return the server lifecycle listener class.
     */
    Class<? extends ServerLifeCycleListener> getServerLifeCycleListenerClass();

}
