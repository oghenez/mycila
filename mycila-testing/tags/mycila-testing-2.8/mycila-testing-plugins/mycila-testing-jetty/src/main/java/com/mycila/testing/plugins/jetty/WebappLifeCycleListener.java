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

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * The webapp lifecycle listener which allow customization of the {@link WebAppContext} configuration.
 */
public interface WebappLifeCycleListener {
    
    /**
     * Handler method which will be called before the {@code WebAppContext} deploy.
     * 
     * @param server
     *            the webapp server.
     * @param webAppContext
     *            the webapp which will be deployed.
     */
    void beforeWebappStart(
            Server server,
            WebAppContext webAppContext);
    

    /**
     * Handler method which will be called after the {@code WebAppContext} is deployed.
     * 
     * @param server
     *            the webapp server.
     * @param webAppContext
     *            the webapp which is deployed.
     */
    void afterWebappStart(
            Server server,
            WebAppContext webAppContext);
    

    /**
     * Handler method which will be called before the {@code WebAppContext} undeploy.
     * 
     * @param server
     *            the webapp server.
     * @param webAppContext
     *            the webapp which will be undeployed.
     */
    void beforeWebappStop(
            Server server,
            WebAppContext webAppContext);
    

    /**
     * Handler method which will be called after the {@code WebAppContext} is undeployed.
     * 
     * @param server
     *            the webapp server.
     * @param webAppContext
     *            the webapp which is undeployed.
     */
    void afterWebappStop(
            Server server,
            WebAppContext webAppContext);
    
}
