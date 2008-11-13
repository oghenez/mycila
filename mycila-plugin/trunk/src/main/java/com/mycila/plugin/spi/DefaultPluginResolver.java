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
import static com.mycila.plugin.spi.PluginUtils.*;
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

    public SortedSet<PluginBinding<T>> getPlugins() {
        return Collections.unmodifiableSortedSet(new TreeSet<PluginBinding<T>>(cache.getBindings().values()));
    }

    public T getPlugin(String name) {
        PluginBinding<T> binding = cache.getBindings().get(name);
        if (binding == null) {
            throw new InexistingPluginException(name);
        }
        return binding.getPlugin();
    }

    public boolean contains(String name) {
        return getPlugins().contains(new Binding<T>(name));
    }

    public SortedMap<String, SortedSet<String>> getMissingDependenciesByPlugin() {
        SortedMap<String, SortedSet<String>> allMiss = new TreeMap<String, SortedSet<String>>();
        SortedSet<PluginBinding<T>> plugins = getPlugins();
        Set<String> loadedPlugins = new HashSet<String>(plugins.size());
        for (PluginBinding<T> plugin : plugins) {
            loadedPlugins.add(plugin.getName());
        }
        for (PluginBinding<T> binding : plugins) {
            SortedSet<String> pluginDependencies = new TreeSet<String>();
            for (String dep : binding.getPlugin().getBefore()) {
                if (!isEmpty(dep)) {
                    pluginDependencies.add(dep);
                }
            }
            for (String dep : binding.getPlugin().getAfter()) {
                if (!isEmpty(dep)) {
                    pluginDependencies.add(dep);
                }
            }
            pluginDependencies.removeAll(loadedPlugins);
            if (!pluginDependencies.isEmpty()) {
                allMiss.put(binding.getName(), Collections.unmodifiableSortedSet(pluginDependencies));
            }
        }
        return Collections.unmodifiableSortedMap(allMiss);
    }

    public SortedSet<String> getMissingDependencies() {
        SortedMap<String, SortedSet<String>> allMiss = getMissingDependenciesByPlugin();
        SortedSet<String> misses = new TreeSet<String>();
        for (SortedSet<String> plugins : allMiss.values()) {
            misses.addAll(plugins);
        }
        return Collections.unmodifiableSortedSet(misses);
    }

    public List<PluginBinding<T>> getResolvedPlugins() {
        List<String> names = getResolvedPluginsName();
        List<PluginBinding<T>> plugins = new ArrayList<PluginBinding<T>>(names.size());
        for (String name : names) {
            plugins.add(cache.getBindings().get(name));
        }
        return Collections.unmodifiableList(plugins);
    }

    public List<String> getResolvedPluginsName() {
        SortedSet<String> missingPlugins = getMissingDependencies();
        DirectedGraph<String, DefaultEdge> graph = new DefaultDirectedWeightedGraph<String, DefaultEdge>(DefaultEdge.class);
        for (PluginBinding<T> binding : getPlugins()) {
            graph.addVertex(binding.getName());
            for (String before : binding.getPlugin().getBefore()) {
                if (!isEmpty(before) && !missingPlugins.contains(before)) {
                    graph.addVertex(before);
                    graph.addEdge(before, binding.getName());
                }
            }
            for (String after : binding.getPlugin().getAfter()) {
                if (!isEmpty(after) && !missingPlugins.contains(after)) {
                    graph.addVertex(after);
                    graph.addEdge(binding.getName(), after);
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
}
