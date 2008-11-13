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
import com.mycila.plugin.api.PluginBinding;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Binding<T extends Plugin> implements PluginBinding<T> {

    private final String name;
    private T plugin;

    Binding(String name) {
        this.name = name;
    }

    Binding<T> withPlugin(T plugin) {
        this.plugin = plugin;
        return this;
    }

    public T getPlugin() {
        return plugin;
    }

    public String getName() {
        return name;
    }

    public int compareTo(PluginBinding<T> o) {
        return getName().compareTo(o.getName());
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || o != null && (o instanceof String || o instanceof Binding) && toString().equals(o.toString());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
