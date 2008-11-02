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

import java.util.Collection;
import java.util.SortedMap;

/**
 * The PluginLoader is responsible of loading all plugins found. Default implementation is from the filesystem, but is could be
 * from other locations
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface PluginLoader<T extends Plugin> {

    /**
     * Load (and reload for subsequent calls) all the available plugins
     *
     * @return a map of plugins by their name, or an empty map
     * @throws DuplicatePluginException If a plugin has been found twice
     * @throws PluginCreationException  If a plugin cannot be created (i.e. bad plugin class)
     * @throws PluginIOException      If errors occured while IO operations
     */
    SortedMap<String, T> loadPlugins();

    /**
     * Set which plugin names should not be loaded
     *
     * @param exclusions An array of plugin names
     */
    void setExclusions(String... exclusions);

    /**
     * Set which plugin names should not be loaded
     *
     * @param exclusions A collection of plugin names
     */
    void setExclusions(Collection<String> exclusions);

    /**
     * Set which classloader should be used to instanciate plugin classes. Usefull to isolate classloaders and plugins.
     *
     * @param loader ClassLoader to use
     */
    void setLoader(ClassLoader loader);
}
