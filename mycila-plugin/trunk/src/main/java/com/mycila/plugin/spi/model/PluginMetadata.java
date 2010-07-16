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

package com.mycila.plugin.spi.model;

import com.mycila.plugin.annotation.ActivateAfter;
import com.mycila.plugin.annotation.ActivateBefore;
import com.mycila.plugin.annotation.Export;
import com.mycila.plugin.annotation.From;
import com.mycila.plugin.annotation.Import;
import com.mycila.plugin.annotation.OnStart;
import com.mycila.plugin.annotation.OnStop;
import com.mycila.plugin.annotation.Plugin;
import com.mycila.plugin.spi.Scopes;
import com.mycila.plugin.spi.internal.ScopeBinding;
import com.mycila.plugin.spi.internal.ScopeResolver;
import com.mycila.plugin.spi.internal.util.AopUtils;
import com.mycila.plugin.spi.invoke.Invokable;
import com.mycila.plugin.spi.invoke.InvokableComposite;
import com.mycila.plugin.spi.invoke.InvokableMember;
import com.mycila.plugin.spi.invoke.Invokables;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
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
    private final Map<Binding<?>, PluginExport<?>> exports = new LinkedHashMap<Binding<?>, PluginExport<?>>();
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

    public <T> PluginExport<T> getExport(Binding<T> binding) throws InexistingBindingException {
        PluginExport<T> export = (PluginExport<T>) exports.get(binding);
        if (export == null)
            throw new InexistingBindingException(pluginClass, binding);
        return export;
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

    private void addExport(String methodName, ScopeBinding scope) {
        Method method = getMethod(methodName);
        InvokableMember<?> invokable = Invokables.get(method, plugin);
        Binding<?> binding = Binding.fromInvokable(invokable);

        Class<?> ret = method.getReturnType();
        if (exports.containsKey(ret))
            throw new DuplicateExportException(pluginClass, ret);
        exports.put(ret, new PluginExport<Object>(
                this,
                ,
                scope));
    }

    private void addInjectionPoint(Method method, List<PluginImport<?>> dependencies) {
        if (!method.getDeclaringClass().isAssignableFrom(pluginClass))
            throw new PluginMetadataException("Method " + method + " is no part of class's hierarchy of " + pluginClass.getName());
        injectionPoints.add(new InjectionPoint(this, Invokables.get(method, plugin), dependencies));
    }

    private void addInjectionPoint(Field field, List<PluginImport<?>> dependencies) {
        if (!field.getDeclaringClass().isAssignableFrom(pluginClass))
            throw new PluginMetadataException("Field " + field + " is no part of class's hierarchy of " + pluginClass.getName());
        injectionPoints.add(new InjectionPoint(this, Invokables.get(field, plugin), dependencies));
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

        public Builder addExport(String methodName, ScopeBinding scope) {
            check();
            metadata.addExport(methodName, scope);
            return this;
        }

        public Builder addInjectionPoint(Method method, List<PluginImport<?>> dependencies) {
            check();
            metadata.addInjectionPoint(method, dependencies);
            return this;
        }

        public Builder addInjectionPoint(Field field, List<PluginImport<?>> dependencies) {
            check();
            metadata.addInjectionPoint(field, dependencies);
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

    static a

    {
        private ScopeResolver scopeResolver = new ScopeResolver(Scopes.DEFAULT);

    public ScopeResolver getScopeResolver() {
        return scopeResolver;
    }

    public void setScopeResolver(ScopeResolver
            scopeResolver) {
        this.scopeResolver = scopeResolver;
    }

    @Override
    public final PluginMetadata getMetadata(Object plugin) {
        Class<?> pluginClass = AopUtils.getTargetClass(plugin.getClass());

        Plugin pluginAnnot = pluginClass.getAnnotation(Plugin.class);

        PluginMetadata.Builder builder = PluginMetadata.create(pluginClass, plugin)
                .withName(pluginAnnot == null || pluginAnnot.name().length() == 0 ? pluginClass.getSimpleName() : pluginAnnot.name())
                .withDescription(pluginAnnot == null || pluginAnnot.description().length() == 0 ? pluginClass.getName() : pluginAnnot.description())
                .withActivateBefore(getActivateBefore(pluginClass))
                .withActivateAfter(getActivateAfter(pluginClass));

        for (Method method : pluginClass.getMethods()) {

            if (method.isAnnotationPresent(OnStart.class))
                builder.addOnStart(method.getName());

            if (method.isAnnotationPresent(OnStop.class))
                builder.addOnStop(method.getName());

            if (method.isAnnotationPresent(Export.class))
                builder.addExport(method.getName(), scopeResolver.getScopeBinding(method));

            if (method.isAnnotationPresent(Import.class)) {
                Type[] types = method.getGenericParameterTypes();
                Annotation[][] annots = method.getParameterAnnotations();
                List<PluginImport> dependencies = new ArrayList<PluginImport>(annots.length);
                for (int i = 0; i < types.length; i++) {
                    Class<?> from = PluginImport.FROM_ANY_PLUGIN;
                    for (Annotation annot : annots[i])
                        if (From.class.isInstance(annot)) {
                            from = ((From) annot).value();
                            break;
                        }
                    dependencies.add(PluginImport.create(types[i], from));
                }
                builder.addInjectionPoint(method.getName(), dependencies);
            }
        }

        Class<?> c = pluginClass;
        while (c != null && c != Object.class) {
            for (Field field : c.getDeclaredFields()) {
                if (field.isAnnotationPresent(Import.class)) {
                    From from = field.getAnnotation(From.class);
                    Class<?> fromPlugin = from == null ? PluginImport.FROM_ANY_PLUGIN : from.value();
                    List<PluginImport> dependencies = new ArrayList<PluginImport>(1);
                    dependencies.add(PluginImport.create(field.getType(), fromPlugin));
                    builder.addInjectionPoint(field, dependencies);
                }
            }
            c = c.getSuperclass();
        }

        return builder.build();
    }

    private Set<Class<?>> getActivateBefore(Class<?> pluginClass) {
        Set<Class<?>> plugins = new HashSet<Class<?>>(2);
        ActivateBefore activateBefore = pluginClass.getAnnotation(ActivateBefore.class);
        if (activateBefore != null && activateBefore.value().length > 0)
            plugins.addAll(Arrays.asList(activateBefore.value()));
        return plugins;
    }

    private Set<Class<?>> getActivateAfter(Class<?> pluginClass) {
        Set<Class<?>> plugins = new HashSet<Class<?>>(2);
        ActivateAfter activateAfter = pluginClass.getAnnotation(ActivateAfter.class);
        if (activateAfter != null && activateAfter.value().length > 0)
            plugins.addAll(Arrays.asList(activateAfter.value()));
        return plugins;
    }
}

}
