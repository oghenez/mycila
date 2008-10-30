package com.mycila.plugin.spi;

import com.mycila.plugin.api.InexistingPluginException;
import com.mycila.plugin.api.Plugin;
import com.mycila.plugin.api.PluginCache;
import com.mycila.plugin.api.PluginResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class DefaultPluginResolver<T extends Plugin> implements PluginResolver<T> {

    final PluginCache<T> cache;

    DefaultPluginResolver(PluginCache<T> cache) {
        this.cache = cache;
    }

    public SortedMap<String, T> getPlugins() {
        return cache.getPlugins();
    }

    public T getPlugin(String name) {
        T plugin = getPlugins().get(name);
        if (plugin == null) {
            throw new InexistingPluginException(name);
        }
        return plugin;
    }

    public boolean contains(String name) {
        return getPlugins().containsKey(name);
    }

    public List<T> getPlugins(String... names) {
        List<T> plugins = new ArrayList<T>(names.length);
        for (String name : names) {
            plugins.add(getPlugin(name));
        }
        return plugins;
    }

    // ???

    public SortedSet<String> getMissingDependencies() {
        return null;
    }

    public List<String> getResolvedDependencies() {
        return null;
    }

    public List<T> getResolvedPlugins() {
        return null;
    }
}
