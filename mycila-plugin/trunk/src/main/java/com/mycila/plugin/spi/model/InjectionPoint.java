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

import com.mycila.plugin.InactivePluginException;
import com.mycila.plugin.InjectionInProgressException;
import com.mycila.plugin.Provider;
import com.mycila.plugin.spi.invoke.InvokableMember;
import com.mycila.plugin.spi.invoke.Invokables;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class InjectionPoint {

    private final PluginMetadata metadata;
    private final InvokableMember<?> invokable;
    private final List<Binding<?>> bindings;
    private volatile boolean injected;

    private InjectionPoint(PluginMetadata metadata, InvokableMember<?> invokable, List<? extends Binding<?>> bindings) {
        this.metadata = metadata;
        this.invokable = invokable;
        this.bindings = Collections.unmodifiableList(new ArrayList<Binding<?>>(bindings));
    }

    public PluginMetadata getPluginMetadata() {
        return metadata;
    }

    public List<Binding<?>> getBindings() {
        return bindings;
    }

    public boolean isInjected() {
        return injected;
    }

    @Override
    public String toString() {
        return "InjectionPoint " + invokable.getMember().getName() + " at " + invokable.getMember().getDeclaringClass().getName();
    }

    static InjectionPoint from(PluginMetadata metadata, Method method, Object plugin) {
        return new InjectionPoint(metadata, Invokables.get(method, plugin), Binding.fromParameters(method));
    }

    static InjectionPoint from(PluginMetadata metadata, Field field, Object plugin) {
        InvokableMember<?> invokable = Invokables.get(field, plugin);
        return new InjectionPoint(metadata, invokable, asList(Binding.fromInvokable(invokable)));
    }

    private static List<? extends Binding<?>> asList(Binding<?> b) {
        return Collections.singletonList(b);
    }

    public void inject(List<PluginExport<?>> dependencies) {
        if (dependencies.size() != bindings.size())
            throw new IllegalArgumentException("Requires " + bindings.size() + " dependencies but " + dependencies.size() + " were injected");
        Object[] oo = new Object[bindings.size()];
        for (int i = 0; i < oo.length; i++) {
            PluginExport<?> export = dependencies.get(i);
            Binding<?> binding = bindings.get(i);
            if (!binding.equals(export.getBinding()))
                throw new IllegalArgumentException("Requires " + binding + " but got " + export.getBinding());
            Provider<?> advisor = ProviderAdvisor.create(export)
                    .addInterceptor(injectedAndStartedInterceptor);
            ProviderProxyFactory factory = ProviderProxyFactory.create(binding, advisor);
            oo[i] = factory.build();
        }
        invokable.invoke(oo);
        injected = true;
    }

    private static final ProviderInterceptor injectedAndStartedInterceptor = new ProviderInterceptor() {
        @Override
        public <T> T get(PluginExport<T> export, Provider<T> next) {
            if (!export.getPluginMetadata().isResolved())
                throw new InjectionInProgressException(export.getPluginMetadata());
            if (!export.getPluginMetadata().isStarted())
                throw new InactivePluginException(export);
            return next.get();
        }
    };

}
