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
     * Get all available plugins
     *
     * @return A map of plugins sorted by their name
     */
    SortedMap<String, T> getPlugins();

    /**
     * Get a specific plugin. Ensure null safe returns.
     *
     * @param name Its name
     * @return The plugin
     * @throws InexistingPluginException If the plugin does not exist
     */
    T getPlugin(String name);

    /**
     * Get a list of plugins by their name. Ensure the returned list size is equals to the argument lenth.
     *
     * @param names The names of the plugins (array list)
     * @return A list of plugins, where the plugin name in names[i] matches the plugin at list<T>.get(i)
     * @throws InexistingPluginException If a plugin name is not found
     */
    List<T> getPlugins(String... names);

    /**
     * Check if a plugin name exist
     *
     * @param name The name of the plugin
     * @return True if the plugin can be retreived
     */
    boolean contains(String name);

    ///////NOTION de before, after...
    /// i.e.: guice depends on jmock, easymock mais p-e aussi d autres...
    // => outil pour definir plugin order ?

    /**
     * List all plugin names, in order of dependencies. I.E. If a plugin A declares to be dependant of plugins B and C,
     * plugins B and C will be listed before plugin A.
     * <p/>
     * Note: cyclic dependencies (i.e. plugin A depend on B, B on C, and C on A) are ignored, meaning that it is the developer role
     * to ensure that his plugin A does not depend on another plugin B which at its turn depends on plugin A
     *
     * @return A list of plugin names, in dependency order, for which no dependencies are missing
     */
    List<String> getResolvedDependencies();

    /**
     * Get the list of plugin names that has been loaded but could cause an execution error because some dependencies have not been found.
     *
     * @return The list of the missing dependencies
     */
    SortedSet<String> getMissingDependencies();

    /**
     * Get the list of plugins in order of dependencies, matching the result of getResolvedDependencies().
     * This method can be used in example to get the list of all the plugins and process them one by one.
     *
     * @return
     */
    List<T> getResolvedPlugins();

}
