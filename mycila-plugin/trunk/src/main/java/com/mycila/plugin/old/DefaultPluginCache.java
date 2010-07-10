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

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class DefaultPluginCache<T extends Plugin> implements PluginCache<T> {

    final ConcurrentHashMap<String, PluginBinding<T>> plugins = new ConcurrentHashMap<String, PluginBinding<T>>();
    final PluginLoader<T> loader;
    volatile boolean loaded;

    DefaultPluginCache(PluginLoader<T> loader) {
        Ensure.notNull("Plugin loader", loader);
        this.loader = loader;
    }

    public void clear() {
        plugins.clear();
        loaded = false;
    }

    public void registerPlugin(String name, T plugin) {
        Ensure.notEmpty("Plugin name", name);
        Ensure.notNull("Plugin instance", plugin);
        plugins.put(name, new Binding<T>(name).withPlugin(plugin));
    }

    public void registerPlugins(Map<String, T> plugins) {
        Ensure.notNull("Plugin map", plugins);
        for (Map.Entry<String, T> entry : plugins.entrySet()) {
            registerPlugin(entry.getKey(), entry.getValue());
        }
    }

    public void removePlugins(String... pluginNames) {
        Ensure.notNull("Plugin names", pluginNames);
        for (String name : pluginNames) {
            this.plugins.remove(name);
        }
    }

    public boolean contains(String pluginName) {
        Ensure.notNull("Plugin name", pluginName);
        return bindings().containsKey(pluginName);
    }

    public Map<String, PluginBinding<T>> getBindings() {
        return Collections.unmodifiableMap(bindings());
    }

    private Map<String, PluginBinding<T>> bindings() {
        if (!loaded) {
            for (PluginBinding<T> binding : loader.loadPlugins())
                plugins.put(binding.getName(), binding);
            loaded = true;
        }
        return plugins;
    }
}
