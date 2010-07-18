package com.mycila.plugin.spi;

import com.mycila.plugin.err.CyclicPluginDependencyException;
import com.mycila.plugin.err.DuplicatePluginException;
import com.mycila.plugin.err.UnresolvedBindingException;
import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.CycleDetector;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.TopologicalOrderIterator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Injector {

    private final Map<Class<?>, PluginMetadata> plugins;
    private final List<Class<?>> order;

    public Injector(Map<Class<?>, PluginMetadata> plugins) {
        this.plugins = plugins;
        this.order = new ArrayList<Class<?>>(plugins.size());
    }

    private void init() throws CyclicPluginDependencyException, UnresolvedBindingException {
        Map<Class<?>, Set<Class<?>>> befores = new HashMap<Class<?>, Set<Class<?>>>();
        Map<Class<?>, Set<Class<?>>> afters = new HashMap<Class<?>, Set<Class<?>>>();

        for (PluginMetadata metadata : plugins.values()) {
            befores.put(metadata.getType(), new LinkedHashSet<Class<?>>(metadata.getBefores()));
            afters.put(metadata.getType(), new LinkedHashSet<Class<?>>(metadata.getAfters()));
        }

        

        DirectedGraph<Class<?>, DefaultEdge> graph = new DefaultDirectedWeightedGraph<Class<?>, DefaultEdge>(DefaultEdge.class);
        for (PluginMetadata metadata : plugins.values()) {
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
    }

    public static Injector build(Iterable<PluginMetadata> metadatas) throws CyclicPluginDependencyException, UnresolvedBindingException {
        Injector injector = new Injector(toMap(metadatas));
        injector.init();
        return injector;
    }

    private static Map<Class<?>, PluginMetadata> toMap(Iterable<PluginMetadata> metadatas) throws DuplicatePluginException {
        Map<Class<?>, PluginMetadata> plugins = new LinkedHashMap<Class<?>, PluginMetadata>();
        for (PluginMetadata metadata : metadatas)
            if (plugins.put(metadata.getType(), metadata) != null)
                throw new DuplicatePluginException(metadata.getType());
        return plugins;
    }

}
