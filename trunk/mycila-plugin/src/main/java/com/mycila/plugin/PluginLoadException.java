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

package com.mycila.plugin;

import static java.lang.String.*;
import java.net.URL;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class PluginLoadException extends PluginRepositoryException {
    private static final long serialVersionUID = 2974368486006410596L;

    public PluginLoadException(String message, URL descriptor, String name, String clazz, Class<?> pluginsType, Throwable e) {
        super(format("%s: %s\n%s", message, e.getMessage(), info(descriptor, name, clazz, pluginsType)), e);
    }

    public PluginLoadException(String message, URL descriptor, String name, String clazz, Class<?> pluginsType) {
        super(format("%s\n%s", message, info(descriptor, name, clazz, pluginsType)));
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