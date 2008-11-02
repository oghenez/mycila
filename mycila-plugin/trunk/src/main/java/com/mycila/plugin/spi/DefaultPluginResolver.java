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
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;

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
            SortedSet<String> pluginDependencies = new TreeSet<String>();
            for (String dep : entry.getValue().getBefore()) {
                if (isPlugin(dep)) {
                    pluginDependencies.add(dep);
                }
            }
            for (String dep : entry.getValue().getAfter()) {
                if (isPlugin(dep)) {
                    pluginDependencies.add(dep);
                }
            }
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
        SortedSet<String> missingPlugins = getMissingDependencies();
        DirectedGraph<String, DefaultEdge> graph = new DefaultDirectedWeightedGraph<String, DefaultEdge>(DefaultEdge.class);
        for (Map.Entry<String, T> entry : getPlugins().entrySet()) {
            graph.addVertex(entry.getKey());
            for (String before : entry.getValue().getBefore()) {
                if (isPlugin(before) && !missingPlugins.contains(before)) {
                    graph.addVertex(before);
                    graph.addEdge(before, entry.getKey());
                }
            }
            for (String after : entry.getValue().getAfter()) {
                if (isPlugin(after) && !missingPlugins.contains(after)) {
                    graph.addVertex(after);
                    graph.addEdge(entry.getKey(), after);
                }
            }
        }
        if (graph.vertexSet().size() == 0) {
            return Collections.emptyList();
        }
        CycleDetector<String, DefaultEdge> detector = new CycleDetector<String, DefaultEdge>(graph);
        if (detector.detectCycles()) {
            SortedMap<String, Plugin> cyclics = new TreeMap<String, Plugin>();
            for (String pluginName : detector.findCycles()) {
                cyclics.put(pluginName, getPlugin(pluginName));
            }
            throw new CyclicDependencyException(cyclics);
        }
        List<String> order = new ArrayList<String>();
        Iterator<String> it = new TopologicalOrderIterator<String, DefaultEdge>(graph);
        while (it.hasNext()) {
            order.add(it.next());
        }
        return Collections.unmodifiableList(order);
    }

    private boolean isPlugin(String name) {
        return name != null && name.trim().length() > 0;
    }

}
