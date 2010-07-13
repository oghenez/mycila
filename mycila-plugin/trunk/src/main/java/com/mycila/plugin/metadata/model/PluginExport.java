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
import com.mycila.plugin.aop.Invokable;
import com.mycila.plugin.aop.InvokableMember;
import com.mycila.plugin.scope.MissingScopeParameterException;
import com.mycila.plugin.scope.ScopeContext;
import com.mycila.plugin.scope.ScopeProvider;
import com.mycila.plugin.scope.ScopeProviders;

import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class PluginExport<T> {

    private final InvokableMember<T> invokable;
    private final PluginMetadata plugin;
    private final Class<? extends ScopeProvider> scopeClass;
    private final Provider<? extends T> provider;

    PluginExport(final PluginMetadata metadata, final InvokableMember<T> invokable, final Class<? extends ScopeProvider> scopeClass, final Map<String, String> parameters) {
        this.plugin = metadata;
        this.invokable = invokable;
        this.scopeClass = scopeClass;
        this.provider = ScopeProviders.build(scopeClass, new ScopeContext() {

            @Override
            public Invokable<?> getInvokable() {
                return invokable;
            }

            @Override
            public boolean hasParameter(String name) {
                return parameters.containsKey(name);
            }

            @Override
            public String getParameter(String name) throws MissingScopeParameterException {
                String val = parameters.get(name);
                if (val == null)
                    throw new MissingScopeParameterException(invokable.getMember(), scopeClass, name);
                return val;
            }

            @Override
            public String toString() {
                return getType().getName();
            }
        });
    }

    public Class<T> getType() {
        return invokable.getType();
    }

    public Class<?> getScope() {
        return scopeClass;
    }

    public PluginMetadata getPluginMetadata() {
        return plugin;
    }

    public Provider<? extends T> getProvider() {
        return provider;
    }

    @Override
    public String toString() {
        return "Export " + getType().getName() + " with scope " + getScope().getSimpleName();
    }

}
