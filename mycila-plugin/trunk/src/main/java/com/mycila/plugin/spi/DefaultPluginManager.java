/**
 * Copyright (C) 2010 Mathieu Carbou <mathieu.carbou@gmail.com>
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

import com.mycila.plugin.CyclicPluginDependencyException;
import com.mycila.plugin.DuplicateExportException;
import com.mycila.plugin.DuplicatePluginException;
import com.mycila.plugin.InvokeException;
import com.mycila.plugin.PluginManager;
import com.mycila.plugin.UnresolvedBindingException;
import com.mycila.plugin.WrappedException;
import com.mycila.plugin.annotation.Export;
import com.mycila.plugin.annotation.Plugin;
import com.mycila.plugin.annotation.scope.Singleton;
import com.mycila.plugin.spi.invoke.Invokable;
import com.mycila.plugin.spi.model.Binding;
import com.mycila.plugin.spi.model.InjectionPoint;
import com.mycila.plugin.spi.model.PluginExport;
import com.mycila.plugin.spi.model.PluginMetadata;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Plugin
public final class DefaultPluginManager implements PluginManager {

    private final List<Invokable<?>> starts = new LinkedList<Invokable<?>>();
    private final List<Invokable<?>> stops = new LinkedList<Invokable<?>>();

    private DefaultPluginManager() {
    }

    @Override
    public void start() throws InvokeException {
        for (Invokable<?> invokable : starts)
            invokable.invoke();
    }

    @Override
    public void stop() throws InvokeException {
        for (Invokable<?> invokable : stops)
            invokable.invoke();
    }

    @Export
    @Singleton
    public PluginManager get() {
        return this;
    }

    public static PluginManager build(Iterable<? extends Class<?>> pluginClasses)
            throws CyclicPluginDependencyException, UnresolvedBindingException, DuplicateExportException, DuplicatePluginException {

        final Map<Class<?>, PluginMetadata> metadatas = new LinkedHashMap<Class<?>, PluginMetadata>();
        final Map<Binding<?>, PluginExport<?>> exports = new LinkedHashMap<Binding<?>, PluginExport<?>>();

        // add this plugin, which is the PluginManager to be able to inject it
        DefaultPluginManager pluginManager = new DefaultPluginManager();
        metadatas.put(PluginManager.class, PluginMetadata.from(pluginManager));

        // load plugins and detect duplicate ones and also duplicate exports
        try {
            for (Class<?> pluginClass : pluginClasses) {
                Object plugin;
                try {
                    plugin = pluginClass.getConstructor().newInstance();
                } catch (InvocationTargetException e) {
                    throw e.getTargetException();
                }
                PluginMetadata metadata = PluginMetadata.from(plugin);
                if (metadatas.put(metadata.getType(), metadata) != null)
                    throw new DuplicatePluginException(metadata.getType());
                for (PluginExport<?> export : metadata.getExports()) {
                    PluginExport old = exports.put(export.getBinding(), export);
                    if (old != null)
                        throw new DuplicateExportException(
                                export.getBinding(),
                                old.getPluginMetadata().getType(),
                                export.getPluginMetadata().getType());
                }
            }
        } catch (Throwable throwable) {
            throw new WrappedException(throwable);
        }

        // find unresolved bindings
        for (PluginMetadata metadata : metadatas.values()) {
            for (InjectionPoint injectionPoint : metadata.getInjectionPoints()) {
                for (Binding<?> binding : injectionPoint.getBindings()) {
                    if (!exports.containsKey(binding))
                        throw new UnresolvedBindingException(metadata.getType(), binding);
                }
            }
        }

        // determine activation order and cyclic dependencies
        final List<Class<?>> order = new LinkedList<Class<?>>();
        DirectedGraph<Class<?>, DefaultEdge> graph = new DefaultDirectedWeightedGraph<Class<?>, DefaultEdge>(DefaultEdge.class);
        for (PluginMetadata metadata : metadatas.values()) {
            graph.addVertex(metadata.getType());
            for (Class<?> before : metadata.getBefores()) {
                graph.addVertex(before);
                graph.addEdge(before, metadata.getType());
            }
            for (Class<?> after : metadata.getAfters()) {
                graph.addVertex(after);
                graph.addEdge(metadata.getType(), after);
            }
        }
        if (!graph.vertexSet().isEmpty()) {
            CycleDetector<Class<?>, DefaultEdge> detector = new CycleDetector<Class<?>, DefaultEdge>(graph);
            if (detector.detectCycles())
                throw new CyclicPluginDependencyException(new TreeSet<Class<?>>(detector.findCycles()));
            for (Iterator<Class<?>> c = new TopologicalOrderIterator<Class<?>, DefaultEdge>(graph); c.hasNext();)
                order.add(c.next());
        }

        // for each plugins inject their dependencies (lazily).
        for (Class<?> pluginClass : order) {
            PluginMetadata metadata = metadatas.get(pluginClass);
            Collection<InjectionPoint> injectionPoints = metadata.getInjectionPoints();
            for (InjectionPoint injectionPoint : injectionPoints) {
                List<PluginExport<?>> dependencies = new ArrayList<PluginExport<?>>(injectionPoints.size());
                for (Binding<?> binding : injectionPoint.getBindings())
                    dependencies.add(exports.get(binding));
                injectionPoint.inject(dependencies);
            }
        }

        // prepare invokables to start and stop plugins
        for (Class<?> pluginClass : order) {
            PluginMetadata metadata = metadatas.get(pluginClass);
            pluginManager.starts.add(metadata.onStart());
            pluginManager.stops.add(0, metadata.onStop());
        }

        // injector ready to be started and stopped
        return pluginManager;
    }

}
