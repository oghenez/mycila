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
public class DefaultPluginLoader<T> implements PluginLoader<T> {

    private final Class<T> pluginsType;
    private final String descriptor;
    private Set<String> exclusions = Collections.emptySet();
    private ClassLoader loader = Thread.currentThread().getContextClassLoader();

    public DefaultPluginLoader(Class<T> pluginsType, String descriptor) {
        this.pluginsType = pluginsType;
        this.descriptor = descriptor.startsWith("/") ? descriptor.substring(1) : descriptor;
    }

    public SortedMap<String, T> loadPlugins() throws PluginRepositoryException {
        SortedMap<String, T> plugins = new TreeMap<String, T>();
        Enumeration<URL> configs = loadDescriptors();
        while (configs.hasMoreElements()) {
            URL descriptor = configs.nextElement();
            Properties p = loadDescriptor(descriptor);
            for (Map.Entry<Object, Object> entry : p.entrySet()) {
                String name = entry.getKey().toString();
                if (!getExclusions().contains(name)) {
                    if (plugins.containsKey(name)) {
                        throw new DuplicatePluginException(descriptor, name);
                    }
                    plugins.put(name, load(descriptor, name, entry.getValue().toString()));
                }
            }
        }
        return plugins;
    }

    protected Enumeration<URL> loadDescriptors() {
        try {
            return getLoader().getResources(getDescriptor());
        } catch (IOException e) {
            throw new PluginRepositoryException(e, "Cannot read plugin descriptors '%s' in classloader '%s'", getDescriptor(), getLoader());
        }
    }

    protected T load(URL descriptor, String name, String clazz) {
        Class<?> c;
        try {
            c = getLoader().loadClass(clazz);
        } catch (Exception e) {
            throw new PluginLoadException("Cannot load the plugin class", descriptor, name, clazz, getPluginsType(), e);
        }
        if (!getPluginsType().isAssignableFrom(c)) {
            throw new PluginLoadException("Loaded plugin class does not match expected plugin type", descriptor, name, clazz, getPluginsType());
        }
        try {
            //noinspection unchecked
            return (T) c.newInstance();
        } catch (Exception e) {
            throw new PluginLoadException("Plugin instanciation error", descriptor, name, clazz, getPluginsType(), e);
        }
    }

    protected Properties loadDescriptor(URL url) {
        InputStream is = null;
        try {
            is = new BufferedInputStream(url.openStream());
            Properties p = new Properties();
            p.load(is);
            return p;
        } catch (IOException e) {
            throw new PluginRepositoryException(e, "Cannot read plugin descriptor '%s'", url);
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

    public Set<String> getExclusions() {
        return exclusions;
    }

    public ClassLoader getLoader() {
        return loader;
    }

    public Class<T> getPluginsType() {
        return pluginsType;
    }

    public String getDescriptor() {
        return descriptor;
    }
}
