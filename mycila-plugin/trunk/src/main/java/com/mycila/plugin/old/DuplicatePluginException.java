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

import static java.lang.String.*;
import java.net.URL;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DuplicatePluginException extends PluginException {
    private static final long serialVersionUID = -2413558322415176455L;
    private final String plugin;
    private final URL descriptor;

    public DuplicatePluginException(URL descriptor, String name) {
        super(format("Duplicate plugin found ! The plugin in this descriptor has already been loaded\n%s", info(descriptor, name)));
        this.descriptor = descriptor;
        this.plugin = name;
    }

    public URL getDescriptor() {
        return descriptor;
    }

    public String getPlugin() {
        return plugin;
    }

    private static String info(URL descriptor, String name) {
        return new StringBuilder()
                .append("- plugins descriptor: ").append(descriptor).append("\n")
                .append("- plugin name: ").append(name).append("\n")
                .toString();
    }
}