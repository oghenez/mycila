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

package com.mycila.plugin.metadata;

import com.mycila.plugin.Invokable;
import com.mycila.plugin.scope.ScopeProvider;
import net.sf.cglib.reflect.FastClass;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class PluginMetadata {

    private final Builder builder;

    private PluginMetadata(Builder builder) {
        this.builder = builder;
    }

    @Override
    public String toString() {
        return "Plugin " + getName() + " (" + getDescription() + ") : " + getType();
    }

    public Object getTarget() {
        return builder.plugin;
    }

    public Class<?> getType() {
        return builder.pluginClass.getJavaClass();
    }

    public String getName() {
        return builder.name;
    }

    public String getDescription() {
        return builder.description;
    }

    public Iterable<? extends Class<?>> getActivateAfter() {
        return builder.activateAfter;
    }

    public Iterable<? extends Class<?>> getActivateBefore() {
        return builder.activateBefore;
    }

    public Iterable<? extends PluginExport<?>> getExports() {
        return builder.exports.values();
    }

    public <T> PluginExport<T> getExport(Class<T> type) throws InexistingExportException, TooManyExportException {
        Class<?> unPerfectMatched = null;
        for (Class<?> key : builder.exports.keySet()) {
            if (key.equals(type))
                return (PluginExport<T>) builder.exports.get(key); // pefect match
            else if (type.isAssignableFrom(key)) {
                if (unPerfectMatched == null)
                    unPerfectMatched = key;
                else
                    throw new TooManyExportException(builder.pluginClass.getJavaClass(), type);
            }
        }
        if (unPerfectMatched == null)
            throw new InexistingExportException(builder.pluginClass.getJavaClass(), type);
        return (PluginExport<T>) builder.exports.get(unPerfectMatched); // the export is a subclass of the requested type
    }

    public Invokable onStart() {
        return builder.onStart;
    }

    public Invokable onStop() {
        return builder.onStop;
    }

    public static Builder create(Class<?> pluginClass, Object plugin) {
        return new Builder(pluginClass, plugin);
    }

    public static final class Builder {

        private final PluginMetadata metadata = new PluginMetadata(this);
        private final FastClass pluginClass;
        private final Object plugin;
        private String name = "";
        private String description = "";
        private final Set<Class<?>> activateAfter = new LinkedHashSet<Class<?>>(2);
        private final Set<Class<?>> activateBefore = new LinkedHashSet<Class<?>>(2);
        private final InvokableComposite onStart = InvokableComposite.empty();
        private final InvokableComposite onStop = InvokableComposite.empty();
        private final Map<Class<?>, PluginExport<?>> exports = new LinkedHashMap<Class<?>, PluginExport<?>>();
        private volatile boolean built;

        private Builder(Class<?> pluginClass, Object plugin) {
            this.pluginClass = FastClass.create(pluginClass);
            this.plugin = plugin;
        }

        public Builder withDescription(String description) {
            check();
            this.description = description;
            return this;
        }

        public Builder withName(String name) {
            check();
            this.name = name;
            return this;
        }

        public Builder withActivateBefore(Collection<Class<?>> plugins) {
            check();
            activateBefore.addAll(plugins);
            return this;
        }

        public Builder withActivateAfter(Collection<Class<?>> plugins) {
            check();
            activateAfter.addAll(plugins);
            return this;
        }

        public Builder addOnStart(String methodName) {
            check();
            try {
                onStart.add(InvokableMethod.create(plugin, pluginClass.getMethod(methodName, new Class[0])));
            } catch (NoSuchMethodError e) {
                throw new PluginMetadataException("Unable to find public method " + methodName + "() in class " + pluginClass.getName() + " with no parameter");
            }
            return this;
        }

        public Builder addOnStop(String methodName) {
            check();
            try {
                onStop.add(InvokableMethod.create(plugin, pluginClass.getMethod(methodName, new Class[0])));
            } catch (NoSuchMethodError e) {
                throw new PluginMetadataException("Unable to find public method " + methodName + "() in class " + pluginClass.getName() + " with no parameter");
            }
            return this;
        }

        public Builder addExport(String methodName, Class<? extends ScopeProvider> scopeClass, Map<String, String> parameters) {
            check();
            try {
                PluginExport export = PluginExport.create(
                        metadata,
                        pluginClass.getMethod(methodName, new Class[0]),
                        scopeClass,
                        parameters);
                if (exports.containsKey(export.getType()))
                    throw new DuplicateExportException(pluginClass.getJavaClass(), export.getType());
                exports.put(export.getType(), export);
            } catch (NoSuchMethodError e) {
                throw new PluginMetadataException("Unable to find public method " + methodName + "() in class " + pluginClass.getName() + " with no parameter");
            }
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
