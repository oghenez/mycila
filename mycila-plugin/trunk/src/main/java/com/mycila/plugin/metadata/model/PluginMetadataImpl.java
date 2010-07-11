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

import com.mycila.plugin.Invokable;
import com.mycila.plugin.metadata.PluginExport;
import com.mycila.plugin.metadata.PluginMetadata;
import com.mycila.plugin.metadata.PluginMetadataException;
import com.mycila.plugin.scope.ScopeProvider;
import net.sf.cglib.reflect.FastClass;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class PluginMetadataImpl implements PluginMetadata {

    private final FastClass pluginClass;
    private final Object plugin;
    private final String name;
    private final String description;
    private final Set<Class<?>> activateAfter = new LinkedHashSet<Class<?>>(2);
    private final Set<Class<?>> activateBefore = new LinkedHashSet<Class<?>>(2);
    private final InvokableComposite onStart = new InvokableComposite();
    private final InvokableComposite onStop = new InvokableComposite();
    private final List<PluginExport<?>> exports = new LinkedList<PluginExport<?>>();

    public PluginMetadataImpl(Class<?> pluginClass, Object plugin, String name, String description) {
        this.pluginClass = FastClass.create(pluginClass);
        this.plugin = plugin;
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Plugin " + getName() + " (" + getDescription() + ") : " + getType();
    }

    @Override
    public Object getTarget() {
        return plugin;
    }

    @Override
    public Class<?> getType() {
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
        return exports;
    }

    @Override
    public Invokable onStart() {
        return onStart;
    }

    @Override
    public Invokable onStop() {
        return onStop;
    }

    public PluginMetadataImpl withActivateBefore(Collection<Class<?>> plugins) {
        activateBefore.addAll(plugins);
        return this;
    }

    public PluginMetadataImpl withActivateAfter(Collection<Class<?>> plugins) {
        activateAfter.addAll(plugins);
        return this;
    }

    public void addOnStart(String methodName) {
        try {
            onStart.add(new InvokableMethod(plugin, pluginClass.getMethod(methodName, new Class[0])));
        } catch (NoSuchMethodError e) {
            throw new PluginMetadataException("Unable to find public method " + methodName + "() in class " + pluginClass.getName() + " with no parameter");
        }
    }

    public void addOnStop(String methodName) {
        try {
            onStop.add(new InvokableMethod(plugin, pluginClass.getMethod(methodName, new Class[0])));
        } catch (NoSuchMethodError e) {
            throw new PluginMetadataException("Unable to find public method " + methodName + "() in class " + pluginClass.getName() + " with no parameter");
        }
    }

    public void addExport(String methodName, Class<? extends ScopeProvider> scopeClass, Map<String, String> parameters) {
        try {
            exports.add(new PluginExportMethod(
                    this,
                    pluginClass.getMethod(methodName, new Class[0]),
                    scopeClass,
                    parameters));
        } catch (NoSuchMethodError e) {
            throw new PluginMetadataException("Unable to find public method " + methodName + "() in class " + pluginClass.getName() + " with no parameter");
        }
    }
}
