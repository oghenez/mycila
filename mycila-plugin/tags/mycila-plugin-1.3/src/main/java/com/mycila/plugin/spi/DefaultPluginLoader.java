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

import com.mycila.plugin.api.*;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import static java.util.Arrays.*;
import java.util.*;
import static java.util.Collections.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class DefaultPluginLoader<T extends Plugin> implements PluginLoader<T> {

    final Class<T> pluginsType;
    final String descriptor;
    Set<String> exclusions = Collections.emptySet();
    ClassLoader loader = Thread.currentThread().getContextClassLoader();

    DefaultPluginLoader(Class<T> pluginsType, String descriptor) {
        this.pluginsType = pluginsType;
        this.descriptor = descriptor.startsWith("/") ? descriptor.substring(1) : descriptor;
    }

    DefaultPluginLoader(Class<T> pluginsType) {
        this(pluginsType, "^%&:;-.`~!@#"); // just to be sure a resource with this name does not exist ;)
    }

    public SortedSet<PluginBinding<T>> loadPlugins() {
        SortedSet<PluginBinding<T>> plugins = new TreeSet<PluginBinding<T>>();
        Enumeration<URL> configs = loadDescriptors();
        while (configs.hasMoreElements()) {
            URL descriptor = configs.nextElement();
            Properties p = loadDescriptor(descriptor);
            for (Map.Entry<Object, Object> entry : p.entrySet()) {
                Binding<T> binding = new Binding<T>(entry.getKey().toString());
                if (!exclusions.contains(binding.getName())) {
                    if (plugins.contains(binding)) {
                        throw new DuplicatePluginException(descriptor, binding.getName());
                    }
                    plugins.add(load(descriptor, binding, entry.getValue().toString()));
                }
            }
        }
        return Collections.unmodifiableSortedSet(plugins);
    }

    Enumeration<URL> loadDescriptors() {
        try {
            return loader.getResources(descriptor);
        } catch (IOException e) {
            throw new PluginIOException(e, "Cannot read plugin descriptors '%s' in classloader '%s'", descriptor, loader);
        }
    }

    Binding<T> load(URL descriptor, Binding<T> binding, String clazz) {
        Class<?> c;
        try {
            c = loader.loadClass(clazz);
        } catch (Exception e) {
            throw new PluginCreationException("Cannot load the plugin class", descriptor, binding.getName(), clazz, pluginsType, e);
        }
        if (!pluginsType.isAssignableFrom(c)) {
            throw new PluginCreationException("Loaded plugin class does not match expected plugin type", descriptor, binding.getName(), clazz, pluginsType);
        }
        try {
            //noinspection unchecked
            return binding.withPlugin((T) c.newInstance());
        } catch (Exception e) {
            throw new PluginCreationException("Plugin instanciation error", descriptor, binding.getName(), clazz, pluginsType, e);
        }
    }

    Properties loadDescriptor(URL url) {
        InputStream is = null;
        try {
            is = new BufferedInputStream(url.openStream());
            Properties p = new Properties();
            p.load(is);
            return p;
        } catch (Exception e) {
            throw new PluginIOException(e, "Cannot read plugin descriptor '%s'", url);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public void setExclusions(String... exclusions) {
        setExclusions(asList(exclusions));
    }

    public void setExclusions(Collection<String> exclusions) {
        this.exclusions = unmodifiableSet(new TreeSet<String>(exclusions));
    }

    public void setLoader(ClassLoader loader) {
        this.loader = loader;
    }

}
