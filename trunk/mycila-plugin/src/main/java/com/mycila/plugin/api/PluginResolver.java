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

import java.util.List;
import java.util.SortedMap;

/**
 * The PluginResolver is responsible of checking for module dependencies to resolve missing dependencies,
 * runnable plugins and provides methods to facilitate plugin access.
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface PluginResolver<T extends Plugin> {

    /**
     * Get a specific plugin. Ensure null safe returns.
     *
     * @param name The plugin name
     * @return The plugin instance
     * @throws InexistingPluginException The plugin does not exist
     */
    T getPlugin(String name);

    /**
     * Get all available plugins
     *
     * @return A map of plugins sorted by their name
     */
    SortedMap<String, T> getPlugins();

    /**
     * Get a list of plugins by their name. Ensure the returned list size is equals to the argument lenth.
     *
     * @param names The names of the plugins (array list)
     * @return A list of plugins, where the plugin name in names[i] matches the plugin at list<T>.get(i)
     * @throws InexistingPluginException A plugin name has not been found
     */
    List<T> getPlugins(String... names);

    /**
     * Check if a plugin name exist
     *
     * @param name The name of the plugin
     * @return True if the plugin can be retreived
     */
    boolean contains(String name);

    /**
     * Get the list of plugin names that have been declared as dependencies in {@link Plugin#getExecutionOrder()}
     * but that have not been loaded (not found, inexisting, ...)
     *
     * @return The list of the missing dependencies. The key is the plugin name and the array is the list of
     *         dependencies not found for this plugin
     */
    SortedMap<String, List<String>> getMissingDependencies();

    /**
     * List all plugin names, in order of dependencies / execution order, thanks to {@link Plugin#getExecutionOrder()}
     * <p/>
     * I.E. If a plugin A declares to be dependant of plugins B and C, plugins B and C will be listed before plugin A.
     * <p/>
     * Notes:
     * <p/>
     * - cyclic dependencies (i.e. plugin A depend on B, B on C, and C on A) are detected and an exception is thrown.<br/>
     * - this method also returns the name of the plugins for which some dependencies have not beed found in the execution order.
     * But the missing dependencies are not returned.<br/>
     * I.E.: If plugin A depends on inexisting plugin B, plugin A is returned in the list, but not plugin B. Plugin B will be
     * returned when calling {@link #getMissingDependencies()}.
     *
     * @return A list of plugin names, in dependency order
     * @throws CyclicDependencyException A cyclic dependency has been found
     */
    List<String> getResolvedPluginsName();

    /**
     * Get the list of plugins in order of dependencies, resolved by {@link #getResolvedPluginsName()}
     *
     * @return the list of plugin instances in order of execution
     */
    List<T> getResolvedPlugins();

}
