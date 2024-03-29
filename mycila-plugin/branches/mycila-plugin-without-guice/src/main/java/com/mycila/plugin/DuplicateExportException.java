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

package com.mycila.plugin;

import com.mycila.plugin.spi.model.Binding;

import java.util.Arrays;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DuplicateExportException extends PluginException {
    private static final long serialVersionUID = 1;

    public DuplicateExportException(Class pluginClass, Binding<?> binding) {
        super("Duplicate exports found for same binding " + binding + " in plugin class " + pluginClass.getName());
    }

    public DuplicateExportException(Binding<?> binding, Class<?>... pluginClass) {
        super("Duplicate exports found for same binding " + binding + " in several plugin classes " + Arrays.toString(toNames(pluginClass)));
    }

    private static String[] toNames(Class<?>[] classes) {
        String[] names = new String[classes.length];
        for (int i = 0; i < names.length; i++)
            names[i] = classes[i].getName();
        return names;
    }

}