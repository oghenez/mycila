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

package com.mycila.guice.spi;

import com.google.inject.*;
import com.mycila.guice.CyclicPluginDependencyException;
import com.mycila.guice.InvokeException;
import com.mycila.guice.PluginManager;
import com.mycila.guice.annotation.ActivateAfter;
import com.mycila.guice.annotation.ActivateBefore;
import com.mycila.guice.annotation.OnActivate;
import com.mycila.guice.annotation.Plugin;
import com.mycila.guice.spi.invoke.Invokables;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DefaultPluginManager implements PluginManager {

    private final Injector injector;
    private final List<Invokable<?>> starts = new LinkedList<Invokable<?>>();
    private final List<Invokable<?>> stops = new LinkedList<Invokable<?>>();

    private DefaultPluginManager(Injector injector) {
        this.injector = injector;
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

    @Override
    public Injector getInjector() {
        return injector;
    }

    public static PluginManager build(Iterable<? extends Class<?>> pluginClasses) throws CyclicPluginDependencyException {
        List<Key<?>> plugins = new LinkedList<Key<?>>();
        Injector injector = load(pluginClasses, plugins);
        plugins = sort(plugins);
        DefaultPluginManager pluginManager = new DefaultPluginManager(injector);
        for (Key<?> key : plugins) {
            for (Method method : key.getTypeLiteral().getRawType().getMethods())
                if (method.isAnnotationPresent(OnActivate.class))
                    pluginManager.starts.add(new Invokable<Object>() {
                        @Override
                        public Object invoke(Object... args) throws InvokeException {


                            Invokables.get(method, )
                        }
                    });


            pluginManager.stops.add(0, metadata.onStop());
        }

        // injector ready to be started and stopped
        return pluginManager;
    }

    private static Key<?> getKey(Class<?> pluginClass) {
        Plugin plugin = pluginClass.getAnnotation(Plugin.class);
        return plugin == null ? Key.get(pluginClass, Plugin.class) : Key.get(pluginClass, plugin);
    }

    private static Injector load(final Iterable<? extends Class<?>> pluginClasses, final List<Key<?>> plugins) {
        return Guice.createInjector(Stage.PRODUCTION, new Module() {
            @Override
            public void configure(Binder binder) {
                for (Class<?> pluginClass : pluginClasses) {
                    Key<?> key = getKey(pluginClass);
                    plugins.add(key);
                    binder.bind(key).to(pluginClass).in(Singleton.class);
                }
            }
        });
    }

    private static List<Key<?>> sort(List<Key<?>> plugins) {
        final List<Key<?>> order = new LinkedList<Key<?>>();
        final DirectedGraph<Key<?>, DefaultEdge> graph = new DefaultDirectedWeightedGraph<Key<?>, DefaultEdge>(DefaultEdge.class);
        for (Key<?> pluginKey : plugins) {
            graph.addVertex(pluginKey);
            {
                ActivateAfter activateAfter = pluginKey.getTypeLiteral().getRawType().getAnnotation(ActivateAfter.class);
                if (activateAfter != null)
                    for (Class<?> pluginToBeActivatedBefore : activateAfter.value()) {
                        Key<?> key = getKey(pluginToBeActivatedBefore);
                        graph.addVertex(key);
                        graph.addEdge(key, pluginKey);
                    }
            }
            {
                ActivateBefore activateBefore = pluginKey.getTypeLiteral().getRawType().getAnnotation(ActivateBefore.class);
                if (activateBefore != null)
                    for (Class<?> pluginToBeActivatedAfter : activateBefore.value()) {
                        Key<?> key = getKey(pluginToBeActivatedAfter);
                        graph.addVertex(key);
                        graph.addEdge(pluginKey, key);
                    }
            }
        }
        if (!graph.vertexSet().isEmpty()) {
            CycleDetector<Key<?>, DefaultEdge> detector = new CycleDetector<Key<?>, DefaultEdge>(graph);
            if (detector.detectCycles())
                throw new CyclicPluginDependencyException(new TreeSet<Key<?>>(detector.findCycles()));
            for (Iterator<Key<?>> c = new TopologicalOrderIterator<Key<?>, DefaultEdge>(graph); c.hasNext();)
                order.add(c.next());
        }
        return order;
    }
}