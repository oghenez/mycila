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

package com.mycila.plugin.metadata.model;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class PluginImport {
    public static final Class<?> FROM_ANY_PLUGIN = Void.TYPE;

    private final Class<?> type;
    private final Class<?> plugin;

    private PluginImport(Class<?> type, Class<?> plugin) {
        this.type = type;
        this.plugin = plugin;
    }

    public Class<?> getPlugin() {
        return plugin;
    }

    public boolean acceptPlugin(Class<?> plugin) {
        return plugin == FROM_ANY_PLUGIN || this.plugin.equals(plugin);
    }

    public Class<?> getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Import " + type.getName() + " from " + (plugin == FROM_ANY_PLUGIN ? "any plugin" : "plugin" + plugin.getName());
    }

    public static PluginImport create(Class<?> type, Class<?> plugin) {
        return new PluginImport(type, plugin);
    }

}
