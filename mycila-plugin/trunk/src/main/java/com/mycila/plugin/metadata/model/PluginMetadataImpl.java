package com.mycila.plugin.metadata.model;

import com.mycila.plugin.Invokable;
import com.mycila.plugin.metadata.InvokeException;
import com.mycila.plugin.metadata.PluginExport;
import com.mycila.plugin.metadata.PluginMetadata;
import com.mycila.plugin.metadata.PluginMetadataException;
import net.sf.cglib.reflect.FastClass;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class PluginMetadataImpl<T> implements PluginMetadata<T> {

    private final FastClass pluginClass;
    private final T plugin;
    private final String name;
    private final String description;
    private final Set<Class<?>> activateAfter = new LinkedHashSet<Class<?>>(2);
    private final Set<Class<?>> activateBefore = new LinkedHashSet<Class<?>>(2);
    private final List<Invokable<?>> onStart = new LinkedList<Invokable<?>>();
    private final List<Invokable<?>> onStop = new LinkedList<Invokable<?>>();

    public PluginMetadataImpl(Class<T> pluginClass, T plugin, String name, String description) {
        this.pluginClass = FastClass.create(pluginClass);
        this.plugin = plugin;
        this.name = name;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PluginMetadataImpl that = (PluginMetadataImpl) o;
        return pluginClass.equals(that.pluginClass);
    }

    @Override
    public int hashCode() {
        return pluginClass.hashCode();
    }

    @Override
    public String toString() {
        return "PluginMetadata of " + pluginClass.getName();
    }

    @Override
    public T getPlugin() {
        return plugin;
    }

    @Override
    public Class<T> getPluginClass() {
        return pluginClass.getJavaClass();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Iterable<? extends Class<?>> getActivateAfter() {
        return activateAfter;
    }

    @Override
    public Iterable<? extends Class<?>> getActivateBefore() {
        return activateBefore;
    }

    @Override
    public Iterable<? extends PluginExport<?>> getExports() {
        return null;
    }

    @Override
    public void onStart() throws InvokeException {
        for (Invokable<?> invokable : onStart)
            invokable.invoke();
    }

    @Override
    public void onStop() throws InvokeException {
        for (Invokable<?> invokable : onStop)
            invokable.invoke();
    }

    public PluginMetadataImpl<T> withActivateBefore(Collection<Class<?>> plugins) {
        activateBefore.addAll(plugins);
        return this;
    }

    public PluginMetadataImpl<T> withActivateAfter(Collection<Class<?>> plugins) {
        activateAfter.addAll(plugins);
        return this;
    }

    public void addOnStart(String methodName) {
        try {
            onStart.add(new InvokableMethod<Object>(plugin, pluginClass.getMethod(methodName, new Class[0])));
        } catch (NoSuchMethodError e) {
            throw new PluginMetadataException("Unable to find public method " + methodName + "() in class " + pluginClass.getName() + " with no parameter");
        }
    }

    public void addOnStop(String methodName) {
        try {
            onStop.add(new InvokableMethod<Object>(plugin, pluginClass.getMethod(methodName, new Class[0])));
        } catch (NoSuchMethodError e) {
            throw new PluginMetadataException("Unable to find public method " + methodName + "() in class " + pluginClass.getName() + " with no parameter");
        }
    }
}
