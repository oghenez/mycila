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

/**
 * The server lifecycle listener which allow customization of the server configuration.
 */
public interface ServerLifeCycleListener {

    /**
     * Handler method which will be called before the {@code Server} starts.
     * 
     * @param server
     *        the server which will start.
     */
    void serverStarting(
            Server server);


    /**
     * Handler method which will be called after the {@code Server} started.
     * 
     * @param server
     *        the server which is started.
     */
    void serverStarted(
            Server server);


    /**
     * Handler method which will be called before the {@code Server} stops.
     * 
     * @param server
     *        the server which will stop.
     */
    void serverStopping(
            Server server);


    /**
     * Handler method which will be called after the {@code Server} stopped.
     * 
     * @param server
     *        the server which is stopped.
     */
    void serverStopped(
            Server server);

}
