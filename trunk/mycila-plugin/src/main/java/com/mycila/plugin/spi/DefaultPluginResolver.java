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

    public SortedMap<String, SortedSet<String>> getMissingDependenciesByPlugin() {
        SortedMap<String, T> plugins = getPlugins();
        SortedMap<String, SortedSet<String>> allMiss = new TreeMap<String, SortedSet<String>>();
        for (Map.Entry<String, T> entry : plugins.entrySet()) {
            SortedSet<String> pluginDependencies = getPluginDependencies(entry.getValue());
            pluginDependencies.removeAll(plugins.keySet());
            if (!pluginDependencies.isEmpty()) {
                allMiss.put(entry.getKey(), pluginDependencies);
            }
        }
        return allMiss;
    }

    public SortedSet<String> getMissingDependencies() {
        SortedMap<String, SortedSet<String>> allMiss = getMissingDependenciesByPlugin();
        SortedSet<String> misses = new TreeSet<String>();
        for (SortedSet<String> plugins : allMiss.values()) {
            misses.addAll(plugins);
        }
        return misses;
    }

    public List<T> getResolvedPlugins() {
        List<String> names = getResolvedPluginsName();
        return getPlugins(names.toArray(new String[names.size()]));
    }

    public List<String> getResolvedPluginsName() {
        SortedMap<String, T> plugins = getPlugins();
        List<Map.Entry<String, T>> pluginsInOrder = new ArrayList<Map.Entry<String, T>>(plugins.size());
        List<Map.Entry<String, T>> independantPlugins = new ArrayList<Map.Entry<String, T>>(plugins.size());
        SortedSet<String> missingPlugins = getMissingDependencies();
        for (Map.Entry<String, T> pluginEntry : plugins.entrySet()) {
            SortedSet<String> pluginDependencies = getPluginDependencies(pluginEntry.getValue());
            pluginDependencies.removeAll(missingPlugins);
            if (pluginDependencies.isEmpty()) {
                independantPlugins.add(pluginEntry);
            } else {
                insert(pluginsInOrder, pluginEntry);
            }
        }
        for (Map.Entry<String, T> pluginEntry : independantPlugins) {
            insert(pluginsInOrder, pluginEntry);
        }

        return getPluginsNames(pluginsInOrder);
    }

    private List<String> getPluginsNames(List<Map.Entry<String, T>> pluginsInOrder) {
        List<String> order = new ArrayList<String>(pluginsInOrder.size());
        for (Map.Entry<String, T> entry : pluginsInOrder) {
            order.add(entry.getKey());
        }
        return order;
    }

    private void insert(List<Map.Entry<String, T>> pluginsInOrder, Map.Entry<String, T> pluginEntry) {
        int i = 0;
        while (i < pluginsInOrder.size()
                && !pluginsInOrder.get(i).getValue().getBefore().contains(pluginEntry.getKey())
                && !pluginEntry.getValue().getAfter().contains(pluginsInOrder.get(i).getKey())) {
            i++;
        }
        while (i > 0
                && !pluginsInOrder.get(i - 1).getValue().getAfter().contains(pluginEntry.getKey())
                && !pluginEntry.getValue().getBefore().contains(pluginsInOrder.get(i - 1).getKey())) {
            i--;
        }
        checkCyclicDependencies(pluginsInOrder, pluginEntry, i);
        pluginsInOrder.add(i, pluginEntry);
    }

    private void checkCyclicDependencies(List<Map.Entry<String, T>> pluginsInOrder, Map.Entry<String, T> pluginEntry, int insertionPosition) {
        for (int i = insertionPosition; i < pluginsInOrder.size(); i++) {
            if (pluginEntry.getValue().getBefore().contains(pluginsInOrder.get(i).getKey())
                    || pluginsInOrder.get(i).getValue().getAfter().contains(pluginEntry.getKey())) {
                throw new CyclicDependencyException(getPluginsNames(pluginsInOrder), pluginEntry.getKey(), pluginEntry.getValue(), i);
            }
        }
    }

    private boolean isPlugin(String name) {
        return name != null && name.trim().length() > 0;
    }

    private SortedSet<String> getPluginDependencies(T plugin) {
        SortedSet<String> pluginDependencies = new TreeSet<String>();
        for (String dep : plugin.getBefore()) {
            if (isPlugin(dep)) {
                pluginDependencies.add(dep);
            }
        }
        for (String dep : plugin.getAfter()) {
            if (isPlugin(dep)) {
                pluginDependencies.add(dep);
            }
        }
        return pluginDependencies;
    }

}
