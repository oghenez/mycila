package com.mycila.plugin;

import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class DefaultPluginCache<T> implements PluginCache<T> {

    private final SortedMap<String, T> plugins = new TreeMap<String, T>();
    private final PluginLoader<T> loader;
    private boolean loaded;

    public DefaultPluginCache(PluginLoader<T> loader) {
        this.loader = loader;
    }

    public void clear() {
        synchronized (plugins) {
            plugins.clear();
            loaded = false;
        }
    }

    public void setPlugin(String name, T plugin) {
        plugins.put(name, plugin);
    }

    public void setPlugins(Map<String, T> plugins) {
        this.plugins.putAll(plugins);
    }

    public SortedMap<String, T> getPlugins() {
        if (!loaded) {
            synchronized (plugins) {
                if (!loaded) {
                    plugins.putAll(getLoader().loadPlugins());
                    loaded = true;
                }
            }
        }
        return Collections.unmodifiableSortedMap(plugins);
    }

    public PluginLoader<T> getLoader() {
        return loader;
    }
}
