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

import com.mycila.plugin.PluginManager;
import com.mycila.plugin.err.CyclicPluginDependencyException;
import com.mycila.plugin.err.DuplicateExportException;
import com.mycila.plugin.err.DuplicatePluginException;
import com.mycila.plugin.err.InvokeException;
import com.mycila.plugin.err.UnresolvedBindingException;
import com.mycila.plugin.err.WrappedException;
import com.mycila.plugin.spi.internal.ClassUtils;
import com.mycila.plugin.spi.internal.invoke.Invokable;
import com.mycila.plugin.spi.internal.model.Binding;
import com.mycila.plugin.spi.internal.model.InjectionPoint;
import com.mycila.plugin.spi.internal.model.PluginExport;
import com.mycila.plugin.spi.internal.model.PluginMetadata;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;

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
public final class DefaultPluginManager implements PluginManager {

    private final List<Invokable<?>> starts;
    private final List<Invokable<?>> stops;

    private DefaultPluginManager(List<Invokable<?>> starts, List<Invokable<?>> stops) {
        this.starts = starts;
        this.stops = stops;
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

    public static DefaultPluginManager build(Iterable<? extends Class<?>> pluginClasses)
            throws CyclicPluginDependencyException, UnresolvedBindingException, DuplicateExportException, DuplicatePluginException {

        final Map<Class<?>, PluginMetadata> metadatas = new LinkedHashMap<Class<?>, PluginMetadata>();
        final Map<Binding<?>, PluginExport<?>> exports = new LinkedHashMap<Binding<?>, PluginExport<?>>();

        // load plugins and detect duplicate ones and also duplicate exports
        try {
            for (Class<?> pluginClass : pluginClasses) {
                PluginMetadata metadata = PluginMetadata.from(ClassUtils.instanciate(pluginClass));
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
        final List<Invokable<?>> starts = new ArrayList<Invokable<?>>(order.size());
        final List<Invokable<?>> stops = new ArrayList<Invokable<?>>(order.size());
        for (Class<?> pluginClass : order) {
            PluginMetadata metadata = metadatas.get(pluginClass);
            starts.add(metadata.onStart());
            stops.add(0, metadata.onStop());
        }

        // injector ready to be started and stopped
        return new DefaultPluginManager(starts, stops);
    }

}
