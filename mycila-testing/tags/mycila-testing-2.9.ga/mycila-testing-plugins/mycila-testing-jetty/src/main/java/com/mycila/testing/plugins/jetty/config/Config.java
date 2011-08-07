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

import java.lang.reflect.Method;
import java.net.URL;

import com.mycila.testing.plugins.jetty.JettyRunWar;
import com.mycila.testing.plugins.jetty.ServerLifeCycleListener;

/**
 * Extension of {@link RawConfig} which provides resolved,
 * <ul>
 * <li>{@link #getWarLocation()} as {@link #getWarLocationUrl()};</li>
 * <li>{@link #getServerLifeCycleListenerClass()} as {@link #getServerLifeCycleListener()} instance.</li>
 * </ul>
 */
public interface Config
        extends RawConfig {

    /**
     * The location of the WAR file to load.
     * 
     * @return the location of the WAR file to load.
     */
    URL getWarLocationUrl();


    /**
     * The {@link ServerLifeCycleListener} instance which allow customization of the server configuration.
     * 
     * @return the server lifecycle listener instance.
     */
    ServerLifeCycleListener getServerLifeCycleListener();


    /**
     * Returns the {@link JettyRunWar} source configuration.
     * 
     * @return the {@link JettyRunWar} source configuration.
     */
    JettyRunWar getSourceConfig();


    /**
     * Returns the {@link JettyRunWar} source configuration.
     * 
     * @return the {@link JettyRunWar} source configuration.
     */
    Method getSourceMethod();


    /**
     * Returns the {@link JettyRunWar} source class.
     * 
     * @return the {@link JettyRunWar} source class.
     */
    Class<?> getSourceClass();

}
