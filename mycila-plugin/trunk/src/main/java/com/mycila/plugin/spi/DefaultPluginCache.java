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

package com.mycila.plugin.spi;

import com.mycila.plugin.api.Plugin;
import com.mycila.plugin.api.PluginCache;
import com.mycila.plugin.api.PluginLoader;

import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class DefaultPluginCache<T extends Plugin> implements PluginCache<T> {

    final SortedMap<String, T> plugins = new TreeMap<String, T>();
    final PluginLoader<T> loader;
    boolean loaded;

    DefaultPluginCache(PluginLoader<T> loader) {
        this.loader = loader;
    }

    public void clear() {
        synchronized (plugins) {
            plugins.clear();
            loaded = false;
        }
    }

    public void registerPlugin(String name, T plugin) {
        plugins.put(name, plugin);
    }

    public void registerPlugins(Map<String, T> plugins) {
        this.plugins.putAll(plugins);
    }

    public void removePlugins(String... pluginNames) {
        for (String name : pluginNames) {
            this.plugins.remove(name);
        }
    }

    public SortedMap<String, T> getPlugins() {
        if (!loaded) {
            synchronized (plugins) {
                if (!loaded) {
                    registerPlugins(loader.loadPlugins());
                    loaded = true;
                }
            }
        }
        return Collections.unmodifiableSortedMap(plugins);
    }
}
