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

package com.mycila.plugin.spi.model;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class PluginImport<T> {
    public static final Class<?> FROM_ANY_PLUGIN = Void.TYPE;

    private final Class<?> fromPlugin;
    private final Binding<T> binding;

    PluginImport(Class<?> fromPlugin, Binding<T> binding) {
        this.binding = binding;
        this.fromPlugin = fromPlugin;
    }

    public Binding<T> getBinding() {
        return binding;
    }

    public Class<?> getPlugin() {
        return fromPlugin;
    }

    public boolean acceptPlugin(Class<?> plugin) {
        return plugin == FROM_ANY_PLUGIN || this.fromPlugin.equals(plugin);
    }

    @Override
    public String toString() {
        return "Import " + binding + " from " + (fromPlugin == FROM_ANY_PLUGIN ? "any plugin" : "plugin " + fromPlugin.getName());
    }

}
