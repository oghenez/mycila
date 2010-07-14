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

package com.mycila.plugin.discovery;

import com.mycila.plugin.discovery.support.ServiceClassLoader;
import com.mycila.plugin.util.ClassUtils;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class JdkServicePluginDiscovery implements PluginDiscovery {

    private final Class<?> markerClass;
    private final ClassLoader classLoader;

    public JdkServicePluginDiscovery(Class<?> markerClass, ClassLoader classLoader) {
        this.markerClass = markerClass;
        this.classLoader = classLoader;
    }

    @Override
    public Iterable<? extends Class<?>> scan() {
        return ServiceClassLoader.load(markerClass, classLoader);
    }
}
