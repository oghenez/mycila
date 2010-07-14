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

import com.mycila.plugin.Provider;
import com.mycila.plugin.invoke.InvokableMember;
import com.mycila.plugin.scope.ScopeBinding;

import java.lang.annotation.Annotation;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class PluginExport<T> {

    private final Class<T> type;
    private final PluginMetadata plugin;
    private final Provider<T> provider;
    private final ScopeBinding scopeBinding;

    PluginExport(final PluginMetadata metadata, final InvokableMember<T> invokable, ScopeBinding scopeBinding) {
        this.plugin = metadata;
        this.type = invokable.getType();
        this.scopeBinding = scopeBinding;
        this.provider = scopeBinding.getScope().getProvider(scopeBinding.getAnnotation(), new Provider<T>() {
            @Override
            public T get() {
                return invokable.invoke();
            }
        });
    }

    public Class<T> getType() {
        return type;
    }

    public PluginMetadata getPluginMetadata() {
        return plugin;
    }

    public Provider<T> getProvider() {
        return provider;
    }

    @Override
    public String toString() {
        return scopeBinding + " " + getType().getName();
    }

    public Annotation getScope() {
        return scopeBinding.getAnnotation();
    }
}
