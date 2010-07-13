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

package com.mycila.plugin.metadata.model;

import com.mycila.plugin.invoke.Invokable;
import com.mycila.plugin.invoke.InvokableComposite;
import com.mycila.plugin.invoke.Invokables;
import com.mycila.plugin.scope.ScopeProvider;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class PluginMetadata {

    private final Class<?> pluginClass;
    private final Object plugin;
    private final Set<Class<?>> activateAfter = new LinkedHashSet<Class<?>>(2);
    private final Set<Class<?>> activateBefore = new LinkedHashSet<Class<?>>(2);
    private final InvokableComposite<?> onStart = Invokables.composite();
    private final InvokableComposite<?> onStop = Invokables.composite();
    private final Map<Class<?>, PluginExport<?>> exports = new LinkedHashMap<Class<?>, PluginExport<?>>();
    private final List<InjectionPoint> injectionPoints = new LinkedList<InjectionPoint>();
    private String name;
    private String description;

    private PluginMetadata(Class<?> pluginClass, Object plugin) {
        this.pluginClass = pluginClass;
        this.plugin = plugin;
        this.name = pluginClass.getSimpleName();
        this.description = pluginClass.getName();
    }

    @Override
    public String toString() {
        return "Plugin " + getName() + " (" + getDescription() + ") : " + getType();
    }

    public Object getTarget() {
        return plugin;
    }

    public Class<?> getType() {
        return pluginClass;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Iterable<Class<?>> getActivateAfter() {
        return activateAfter;
    }

    public Iterable<Class<?>> getActivateBefore() {
        return activateBefore;
    }

    public Iterable<PluginExport<?>> getExports() {
        return exports.values();
    }

    public <T> PluginExport<T> getExport(Class<T> type) throws InexistingExportException, TooManyExportException {
        Class<?> unPerfectMatched = null;
        for (Class<?> key : exports.keySet()) {
            if (key.equals(type))
                return (PluginExport<T>) exports.get(key); // pefect match
            else if (type.isAssignableFrom(key)) {
                if (unPerfectMatched == null)
                    unPerfectMatched = key;
                else
                    throw new TooManyExportException(pluginClass, type);
            }
        }
        if (unPerfectMatched == null)
            throw new InexistingExportException(pluginClass, type);
        return (PluginExport<T>) exports.get(unPerfectMatched); // the export is a subclass of the requested type
    }

    public Iterable<InjectionPoint> getInjectionPoints() {
        return injectionPoints;
    }

    public Invokable<?> onStart() {
        return onStart;
    }

    public Invokable<?> onStop() {
        return onStop;
    }

    private void addExport(String methodName, Class<? extends ScopeProvider> scopeClass, Map<String, String> parameters) {
        Method method = getMethod(methodName);
        Class<?> ret = method.getReturnType();
        if (exports.containsKey(ret))
            throw new DuplicateExportException(pluginClass, ret);
        exports.put(ret, new PluginExport<Object>(
                this,
                Invokables.get(method, plugin),
                scopeClass,
                parameters));
    }

    private void addInjectionPoint(String methodName, List<PluginImport> dependencies) {
        Class<?>[] params = new Class<?>[dependencies.size()];
        for (int i = 0; i < params.length; i++)
            params[i] = dependencies.get(i).getType();
        Method method = getMethod(methodName, params);
        injectionPoints.add(new InjectionPoint(this, Invokables.get(method, plugin), dependencies));
    }

    private <T> void add(InvokableComposite<T> onEvent, String methodName) {
        onEvent.add(Invokables.<T>get(getMethod(methodName), plugin));
    }

    private Method getMethod(String name, Class<?>... paramTypes) {
        try {
            return pluginClass.getMethod(name, paramTypes);
        } catch (NoSuchMethodException e) {
            throw new PluginMetadataException("Unable to find public method " + name + " in class " + pluginClass.getName() + " with parameter types " + Arrays.toString(paramTypes));
        }
    }

    public static Builder create(Class<?> pluginClass, Object plugin) {
        return new Builder(new PluginMetadata(pluginClass, plugin));
    }

    public static final class Builder {
        private final PluginMetadata metadata;
        private volatile boolean built;

        private Builder(PluginMetadata metadata) {
            this.metadata = metadata;
        }

        public Builder withDescription(String description) {
            check();
            this.metadata.description = description;
            return this;
        }

        public Builder withName(String name) {
            check();
            this.metadata.name = name;
            return this;
        }

        public Builder withActivateBefore(Collection<Class<?>> plugins) {
            check();
            metadata.activateBefore.addAll(plugins);
            return this;
        }

        public Builder withActivateAfter(Collection<Class<?>> plugins) {
            check();
            metadata.activateAfter.addAll(plugins);
            return this;
        }

        public Builder addOnStart(String methodName) {
            check();
            metadata.add(metadata.onStart, methodName);
            return this;
        }

        public Builder addOnStop(String methodName) {
            check();
            metadata.add(metadata.onStop, methodName);
            return this;
        }

        public Builder addExport(String methodName, Class<? extends ScopeProvider> scopeClass, Map<String, String> parameters) {
            check();
            metadata.addExport(methodName, scopeClass, parameters);
            return this;
        }

        public Builder addInjectionPoint(String methodName, List<PluginImport> dependencies) {
            check();
            metadata.addInjectionPoint(methodName, dependencies);
            return this;
        }

        public PluginMetadata build() {
            built = true;
            return metadata;
        }

        private void check() {
            if (built)
                throw new ConcurrentModificationException("PluginMetadata cannot be modified after being built");
        }
    }

}
