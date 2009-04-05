/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mycila.plugin.api;

import java.util.Map;

/**
 * The PluginCache is responsible of caching all plugins loaded and add / modify them at runtime.
 * Thus, it is possible to add, remove or replace some plugins.
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface PluginCache<T extends Plugin> {

    boolean contains(String pluginName);

    /**
     * Load the plugins if not done and returns an unmodifiable map of plugins sorted by their names
     *
     * @return The plugins as an unmodifiable map
     */
    Map<String, PluginBinding<T>> getBindings();

    /**
     * Clear the plugin cache, forcing a reload at next getPlugins() call
     */
    void clear();

    /**
     * Install a plugin at runtime in the current cache, overwriting existing one.
     *
     * @param name   The name of the plugin
     * @param plugin The plugin instance
     */
    void registerPlugin(String name, T plugin);

    /**
     * Install some plugins at runtime in the current cache, overwriting existing ones.
     *
     * @param plugins A map of plugins to register
     */
    void registerPlugins(Map<String, T> plugins);

    /**
     * Remove some plugins from the current cache.
     *
     * @param pluginNames the plugins to remove
     */
    void removePlugins(String... pluginNames);

}
