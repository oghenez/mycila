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
        // the list of all missing plugins. used to detect independant plugins (taht do not depends on other ones)
        SortedSet<String> missingPlugins = getMissingDependencies();
        // the final list we build
        List<Map.Entry<String, T>> pluginsInOrder = new ArrayList<Map.Entry<String, T>>();
        // the list of independant plugins
        SortedMap<String, T> independantPlugins = new TreeMap<String, T>();
        // all plugins. we put them in a modifiable list so that we can remove an item when moving it into another list
        SortedMap<String, T> allPlugins = new TreeMap<String, T>(getPlugins());
        // flag to ensure the processing is done correctly. if the algorithm is not ok for a specfic case, an assetion error is thrown
        int allPluginsSizeBeforeProcessing;
        // we iterate until there is some plugins remaining in the list
        while (!allPlugins.isEmpty()) {
            allPluginsSizeBeforeProcessing = allPlugins.size();
            // iterator that can modify the list...
            Iterator<Map.Entry<String, T>> it = allPlugins.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, T> pluginEntry = it.next();
                // first, check if this plugin is an independant dependency. Independant dependencies are put in a special list and
                // used only when needed since we cannot find their exact insert position until we found another plugin depending on one of them.
                SortedSet<String> pluginDependencies = getPluginDependencies(pluginEntry.getValue());
                pluginDependencies.removeAll(missingPlugins);
                if (pluginDependencies.isEmpty()) {
                    // so if this plugin has no dependency, we move it to the independant list.
                    independantPlugins.put(pluginEntry.getKey(), pluginEntry.getValue());
                    it.remove();
                } else if (pluginsInOrder.isEmpty()) {
                    // if this plugin has dependencies and the list is empty, directly insert it in the final list.
                    insert(pluginsInOrder, pluginEntry, independantPlugins);
                    it.remove();
                } else {
                    // here, the lits is not empty, and we want to insert a plugin containing dependencies.
                    // so we search for an insertion position. If the method returns -1, it means that this plugin cannot
                    // be inserted yet because we do not have enough informations in the list to know where to insert it.
                    // so it is left and will be inserted after, in the next while loop.
                    int pos = findInsertPosition(pluginsInOrder, pluginEntry);
                    if (pos != -1) {
                        // if we found an insertion position for this plugin, we move it to the final list.
                        insert(pluginsInOrder, pos, pluginEntry, independantPlugins);
                        it.remove();
                    }
                }
            }
            // when the iterator loop is finished, there can be some plugins remaining in the allPlugins list. They are those
            // which could not be inserted because we did not have enough information to insert them. They can for example
            // depend on independant plugins not yet inserted. So we first the insertion of the first one we find and let the while
            // loop work again.
            if (allPluginsSizeBeforeProcessing == allPlugins.size()) {
                //Map.Entry<String, T> entry = allPlugins.entrySet().iterator().next();
                insert(pluginsInOrder, allPlugins.entrySet().iterator().next(), independantPlugins);
                //allPlugins.remove(entry.getKey());
            }
        }
        // Here, only some independant plugins are remaing, are not used as dependencies elswhere. So we can insert them
        // safely anywhere in the list
        for (Map.Entry<String, T> entry : independantPlugins.entrySet()) {
            pluginsInOrder.add(pluginsInOrder.size(), entry);
        }
        return getPluginsNames(pluginsInOrder);
    }

    private void insert(List<Map.Entry<String, T>> pluginsInOrder, Map.Entry<String, T> pluginEntry, SortedMap<String, T> independantPlugins) {
        insert(pluginsInOrder, pluginsInOrder.size(), pluginEntry, independantPlugins);
    }

    private void insert(List<Map.Entry<String, T>> pluginsInOrder, int pos, Map.Entry<String, T> pluginEntry, SortedMap<String, T> independantPlugins) {
        pluginsInOrder.add(pos, pluginEntry);
        // now, after having inserted a plugin with dependencies, we search in the independant list
        // if there is some plugins that are dependencies for the plugin we have juste inserted.
        for (Map.Entry<String, T> independantPlugin : new TreeMap<String, T>(independantPlugins).entrySet()) {
            int i = findInsertPosition(pluginsInOrder, independantPlugin);
            // if the return is -1, it means that we do not have enough information yet to insert
            // this independant plugin
            if (i != -1) {
                // if an independant plugin can be inserted, it means that the plugin we have inserted declared it
                // has a dependency, so we were able to insert it.
                pluginsInOrder.add(i, independantPlugin);
                independantPlugins.remove(independantPlugin.getKey());
            }
        }
    }

    private int findInsertPosition(List<Map.Entry<String, T>> pluginsInOrder, Map.Entry<String, T> pluginEntry) {
        int i = 0;
        boolean foundMaximumRightPosition = false;
        for (; i < pluginsInOrder.size(); i++) {
            if (pluginsInOrder.get(i).getValue().getBefore().contains(pluginEntry.getKey())
                    || pluginEntry.getValue().getAfter().contains(pluginsInOrder.get(i).getKey())) {
                foundMaximumRightPosition = true;
                break;
            }
        }
        boolean foundMinimumLeftPosition = false;
        for (; i > 0; i--) {
            if (pluginsInOrder.get(i - 1).getValue().getAfter().contains(pluginEntry.getKey())
                    || pluginEntry.getValue().getBefore().contains(pluginsInOrder.get(i - 1).getKey())) {
                foundMinimumLeftPosition = true;
                break;
            }
        }
        if (foundMaximumRightPosition || foundMinimumLeftPosition) {
            checkCyclicDependencies(pluginsInOrder, pluginEntry, i);
            return i;
        }
        return -1;
    }

    private void checkCyclicDependencies(List<Map.Entry<String, T>> pluginsInOrder, Map.Entry<String, T> pluginEntry, int insertionPosition) {
        for (int i = insertionPosition; i < pluginsInOrder.size(); i++) {
            if (pluginEntry.getValue().getBefore().contains(pluginsInOrder.get(i).getKey())
                    || pluginsInOrder.get(i).getValue().getAfter().contains(pluginEntry.getKey())) {
                throw new CyclicDependencyException(getPluginsNames(pluginsInOrder), pluginEntry.getKey(), pluginEntry.getValue(), i);
            }
        }
    }

    private List<String> getPluginsNames(List<Map.Entry<String, T>> pluginsInOrder) {
        List<String> order = new ArrayList<String>(pluginsInOrder.size());
        for (Map.Entry<String, T> entry : pluginsInOrder) {
            order.add(entry.getKey());
        }
        return order;
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
