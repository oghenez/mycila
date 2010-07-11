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

import com.mycila.plugin.PluginException;

import java.net.URL;

import static java.lang.String.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class PluginCreationException extends PluginException {
    private static final long serialVersionUID = 2974368486006410596L;
    private final URL descriptor;
    private final String plugin;
    private final String clazz;
    private final Class<?> pluginType;

    public PluginCreationException(String message, URL descriptor, String name, String clazz, Class<?> pluginsType, Throwable e) {
        super(format("%s: %s\n%s", message, e.getMessage(), info(descriptor, name, clazz, pluginsType)), e);
        this.descriptor = descriptor;
        this.plugin = name;
        this.clazz = clazz;
        this.pluginType = pluginsType;
    }

    public PluginCreationException(String message, URL descriptor, String name, String clazz, Class<?> pluginsType) {
        super(format("%s\n%s", message, info(descriptor, name, clazz, pluginsType)));
        this.descriptor = descriptor;
        this.plugin = name;
        this.clazz = clazz;
        this.pluginType = pluginsType;
    }

    public String getClazz() {
        return clazz;
    }

    public URL getDescriptor() {
        return descriptor;
    }

    public String getPlugin() {
        return plugin;
    }

    public Class<?> getPluginType() {
        return pluginType;
    }

    private static String info(URL descriptor, String name, String clazz, Class<?> pluginsType) {
        return new StringBuilder()
                .append("- plugins descriptor: ").append(descriptor).append("\n")
                .append("- plugins type: ").append(pluginsType.getName()).append("\n")
                .append("- plugin name: ").append(name).append("\n")
                .append("- plugin class: ").append(clazz)
                .toString();
    }

}