package com.mycila.plugin.spi;

import com.mycila.plugin.api.*;

import java.util.*;

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

    public SortedMap<String, List<String>> getMissingDependencies() {
        SortedMap<String, T> plugins = getPlugins();
        SortedMap<String, List<String>> allMiss = new TreeMap<String, List<String>>();
        for (Map.Entry<String, T> entry : plugins.entrySet()) {
            List<String> pluginMiss = new ArrayList<String>(entry.getValue().getExecutionOrder().size());
            for (String dependency : entry.getValue().getExecutionOrder()) {
                if (!plugins.containsKey(dependency)) {
                    pluginMiss.add(dependency);
                }
            }
            if (!pluginMiss.isEmpty()) {
                allMiss.put(entry.getKey(), pluginMiss);
            }
        }
        return allMiss;
    }

    public List<T> getResolvedPlugins() {
        List<String> names = getResolvedPluginsName();
        return getPlugins(names.toArray(new String[names.size()]));
    }

    public List<String> getResolvedPluginsName() {
        SortedMap<String, T> plugins = getPlugins();
        List<String> resolved = new ArrayList<String>(plugins.size());
        for (Map.Entry<String, T> entry : plugins.entrySet()) {
            String name = null;
            for (String dep : resolved) {
                if (entry.getValue().getExecutionOrder().contains(dep)) {
                    name = dep;
                    break;
                }
            }
            if (name == null) {
                resolved.add(resolved.size(), entry.getKey());
            } else {
                resolved.add(resolved.indexOf(entry.getKey()) - 1, name);
            }
        }
        for (int i = 0; i < resolved.size(); i++) {
            String module = resolved.get(i);
            for (int j = i; j < resolved.size(); j++) {
                if (plugins.get(resolved.get(j)).getExecutionOrder().contains(module)) {
                    throw new CyclicDependencyException();
                }
            }
        }
        return resolved;
    }

}
