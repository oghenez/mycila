/**
 * Copyright (C) 2010 Mathieu Carbou <mathieu.carbou@gmail.com>
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

package com.mycila.plugin.old;

import static com.mycila.plugin.old.Ensure.*;

/**
 * Defines a new plugin manager based on a plugin descriptor or
 * memory based if you do not pass a descriptor (i.e. META-INF/plugins.properties)
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class PluginManager<T extends Plugin> {

    private final PluginLoader<T> loader;
    private final PluginResolver<T> resolver;
    private final PluginCache<T> cache;

    public PluginManager(Class<T> pluginType) {
        notNull("Plugin type", pluginType);
        loader = new DefaultPluginLoader<T>(pluginType);
        cache = new DefaultPluginCache<T>(loader);
        resolver = new DefaultPluginResolver<T>(cache);
    }

    public PluginManager(Class<T> pluginType, String pluginDescriptor) {
        notNull("Plugin type", pluginType);
        notEmpty("Plugin descriptor", pluginDescriptor);
        loader = new DefaultPluginLoader<T>(pluginType, pluginDescriptor);
        cache = new DefaultPluginCache<T>(loader);
        resolver = new DefaultPluginResolver<T>(cache);
    }

    public PluginCache<T> getCache() {
        return cache;
    }

    public PluginLoader<T> getLoader() {
        return loader;
    }

    public PluginResolver<T> getResolver() {
        return resolver;
    }
}
