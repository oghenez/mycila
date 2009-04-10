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

import com.mycila.log.Logger;
import com.mycila.log.Loggers;
import com.mycila.plugin.api.DuplicatePluginException;
import com.mycila.plugin.api.Plugin;
import com.mycila.plugin.api.PluginBinding;
import com.mycila.plugin.api.PluginCreationException;
import com.mycila.plugin.api.PluginIOException;
import com.mycila.plugin.api.PluginLoader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import static java.util.Arrays.*;
import java.util.Collection;
import java.util.Collections;
import static java.util.Collections.*;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class DefaultPluginLoader<T extends Plugin> implements PluginLoader<T> {

    private static final Logger LOGGER = Loggers.get(DefaultPluginLoader.class);

    final Class<T> pluginsType;
    final String descriptor;
    Set<String> exclusions = Collections.emptySet();
    ClassLoader loader = Thread.currentThread().getContextClassLoader();

    DefaultPluginLoader(Class<T> pluginsType) {
        this(pluginsType, null); // just to be sure a resource with this name does not exist ;)
    }

    DefaultPluginLoader(Class<T> pluginsType, String descriptor) {
        this.pluginsType = pluginsType;
        this.descriptor = descriptor != null ?
                descriptor.startsWith("/") ? descriptor.substring(1) : descriptor :
                null;
    }

    public Set<PluginBinding<T>> loadPlugins() {
        LOGGER.debug("Loading plugins from descriptors {0}...", descriptor);
        Set<PluginBinding<T>> plugins = new HashSet<PluginBinding<T>>();
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
        LOGGER.debug("Loaded {0} plugins !", plugins.size());
        return Collections.unmodifiableSet(plugins);
    }

    Enumeration<URL> loadDescriptors() {
        try {
            return descriptor == null ?
                    Collections.enumeration(Collections.<URL>emptyList()) :
                    loader.getResources(descriptor);
        } catch (IOException e) {
            throw new PluginIOException(e, "Cannot read plugin descriptors '%s' in classloader '%s'", descriptor, loader);
        }
    }

    @SuppressWarnings({"unchecked"})
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
                } catch (IOException ignored) {
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
