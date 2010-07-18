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

package com.mycila.plugin.spi.internal.model;

import com.mycila.plugin.annotation.ActivateAfter;
import com.mycila.plugin.annotation.ActivateBefore;
import com.mycila.plugin.annotation.Export;
import com.mycila.plugin.annotation.Import;
import com.mycila.plugin.annotation.OnStart;
import com.mycila.plugin.annotation.OnStop;
import com.mycila.plugin.annotation.Plugin;
import com.mycila.plugin.err.DuplicateExportException;
import com.mycila.plugin.err.InexistingBindingException;
import com.mycila.plugin.err.InvokeException;
import com.mycila.plugin.spi.internal.ScopeBinding;
import com.mycila.plugin.spi.internal.ScopeLoader;
import com.mycila.plugin.spi.internal.aop.AopUtilsTOREFAC;
import com.mycila.plugin.spi.internal.invoke.Invokable;
import com.mycila.plugin.spi.internal.invoke.InvokableComposite;
import com.mycila.plugin.spi.internal.invoke.Invokables;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
    private final String name;
    private final String description;
    private final Set<Class<?>> activateAfter = new LinkedHashSet<Class<?>>(2);
    private final Set<Class<?>> activateBefore = new LinkedHashSet<Class<?>>(2);
    private final InvokableComposite<?> onStart = Invokables.composite();
    private final InvokableComposite<?> onStop = Invokables.composite();
    private final Map<Binding<?>, PluginExport<?>> exports = new LinkedHashMap<Binding<?>, PluginExport<?>>();
    private final List<InjectionPoint> injectionPoints = new LinkedList<InjectionPoint>();
    private volatile boolean running;
    private volatile boolean resolved;

    private PluginMetadata(Object plugin, Class<?> c, String name, String description) {
        this.pluginClass = c;
        this.plugin = plugin;
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Plugin " + getName() + " (" + getDescription() + ") : " + pluginClass.getName();
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

    public Collection<Class<?>> getAfters() {
        return Collections.unmodifiableCollection(activateAfter);
    }

    public Collection<Class<?>> getBefores() {
        return Collections.unmodifiableCollection(activateBefore);
    }

    public Collection<PluginExport<?>> getExports() {
        return Collections.unmodifiableCollection(exports.values());
    }

    public Collection<InjectionPoint> getInjectionPoints() {
        return Collections.unmodifiableCollection(injectionPoints);
    }

    public <T> PluginExport<T> getExport(Binding<T> binding) throws InexistingBindingException {
        PluginExport<T> export = (PluginExport<T>) exports.get(binding);
        if (export == null)
            throw new InexistingBindingException(pluginClass, binding);
        return export;
    }

    public Invokable<?> onStart() {
        return new Invokable<Object>() {
            @Override
            public Object invoke(Object... args) throws InvokeException {
                running = false;
                Object ret = onStart.invoke();
                running = true;
                return ret;
            }
        };
    }

    public Invokable<?> onStop() {
        return new Invokable<Object>() {
            @Override
            public Object invoke(Object... args) throws InvokeException {
                running = false;
                Object ret = onStop.invoke();
                running = true;
                return ret;
            }
        };
    }

    public boolean isStarted() {
        return running;
    }

    public boolean isStopped() {
        return !running;
    }

    public boolean isResolved() {
        if (resolved) return true;
        for (InjectionPoint injectionPoint : injectionPoints)
            if (!injectionPoint.isInjected()) return false;
        return resolved = true;
    }

    private PluginMetadata addActivateBefore() {
        ActivateBefore activateBefore = pluginClass.getAnnotation(ActivateBefore.class);
        if (activateBefore != null && activateBefore.value().length > 0)
            this.activateBefore.addAll(Arrays.asList(activateBefore.value()));
        return this;
    }

    private PluginMetadata addActivateAfter() {
        ActivateAfter activateAfter = pluginClass.getAnnotation(ActivateAfter.class);
        if (activateAfter != null && activateAfter.value().length > 0)
            this.activateAfter.addAll(Arrays.asList(activateAfter.value()));
        return this;
    }

    private <T> PluginMetadata add(InvokableComposite<T> onEvent, Method method) {
        onEvent.add(Invokables.<T>get(method, plugin));
        return this;
    }

    private PluginMetadata addExport(Method method, ScopeBinding scope) {
        PluginExport export = PluginExport.export(this, Invokables.get(method, plugin), scope);
        if (exports.containsKey(export.getBinding()))
            throw new DuplicateExportException(pluginClass, export.getBinding());
        exports.put(export.getBinding(), export);
        return this;
    }

    private PluginMetadata addInjectionPoint(Method method) {
        injectionPoints.add(InjectionPoint.from(this, method, plugin));
        return this;
    }

    private PluginMetadata addInjectionPoint(Field field) {
        injectionPoints.add(InjectionPoint.from(this, field, plugin));
        return this;
    }

    public static PluginMetadata from(Object plugin) {
        ScopeLoader scopeResolver = new ScopeLoader();
        Class<?> pluginClass = AopUtilsTOREFAC.getTargetClass(plugin);
        Plugin pluginAnnot = pluginClass.getAnnotation(Plugin.class);
        PluginMetadata metadata = new PluginMetadata(
                plugin,
                pluginClass,
                pluginAnnot == null || pluginAnnot.name().length() == 0 ? pluginClass.getSimpleName() : pluginAnnot.name(),
                pluginAnnot == null || pluginAnnot.description().length() == 0 ? pluginClass.getName() : pluginAnnot.description())
                .addActivateBefore()
                .addActivateAfter();
        for (Method method : pluginClass.getMethods()) {
            if (method.isAnnotationPresent(OnStart.class))
                metadata.add(metadata.onStart, method);
            if (method.isAnnotationPresent(OnStop.class))
                metadata.add(metadata.onStop, method);
            if (method.isAnnotationPresent(Export.class))
                metadata.addExport(method, scopeResolver.loadScopeBinding(method));
            if (method.isAnnotationPresent(Import.class))
                metadata.addInjectionPoint(method);
        }
        for (Class<?> c = pluginClass; c != null && c != Object.class; c = c.getSuperclass())
            for (Field field : c.getDeclaredFields())
                if (field.isAnnotationPresent(Import.class))
                    metadata.addInjectionPoint(field);
        return metadata;
    }

}
