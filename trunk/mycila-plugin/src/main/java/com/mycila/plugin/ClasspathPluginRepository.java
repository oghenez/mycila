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
public class ClasspathPluginRepository<T> implements PluginRepository<T> {

    private final Class<T> pluginsType;
    private final String descriptor;
    private Set<String> exclusions = Collections.emptySet();
    private ClassLoader loader = Thread.currentThread().getContextClassLoader();

    ClasspathPluginRepository(Class<T> pluginsType, String descriptor) {
        this.pluginsType = pluginsType;
        this.descriptor = descriptor.startsWith("/") ? descriptor.substring(1) : descriptor;
    }

    public Map<String, T> loadPlugins() throws PluginRepositoryException {
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
        return unmodifiableSortedMap(plugins);
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
