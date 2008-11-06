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
import java.util.SortedSet;

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
    SortedSet<PluginBinding<T>> getPlugins();

    /**
     * Check if a plugin name exist
     *
     * @param name The name of the plugin
     * @return True if the plugin can be retreived
     */
    boolean contains(String name);

    /**
     * Get the list of plugin names that have been declared as dependencies in {@link Plugin#getBefore()}
     * and {@link com.mycila.plugin.api.Plugin#getAfter()} but that have not been loaded (because not
     * found, inexisting, ...)
     *
     * @return The list of the missing dependencies. The key is the plugin name and the value is the set of
     *         dependencies not found for this plugin.
     */
    SortedMap<String, SortedSet<String>> getMissingDependenciesByPlugin();

    /**
     * Returns all dependencies missing, regardless of all plugins
     *
     * @return A set of plugin names missing
     */
    SortedSet<String> getMissingDependencies();

    /**
     * List all plugin names, in order of dependencies / execution, thanks to {@link Plugin#getBefore()} and
     * {@link Plugin#getAfter()}.
     * <p/>
     * Notes:<br/>
     * - cyclic dependencies (i.e. plugin A depend on B, B on C, and C on A) are detected and an exception is thrown.<br/>
     * - this method only returns plugin names that have been loaded and can be used. So if a plugin A depends on a plugin B
     * in its getBefore declaration, and the plugin B is not loaded, A will be returned in this list but not B.
     *
     * @return A list of plugin names, in dependency order
     * @throws CyclicDependencyException A cyclic dependency has been found
     */
    List<String> getResolvedPluginsName();

    /**
     * Get the list of plugins in order of dependencies, resolved by {@link #getResolvedPluginsName()}.
     *
     * @return the list of plugin instances in order of execution
     */
    List<PluginBinding<T>> getResolvedPlugins();

}
