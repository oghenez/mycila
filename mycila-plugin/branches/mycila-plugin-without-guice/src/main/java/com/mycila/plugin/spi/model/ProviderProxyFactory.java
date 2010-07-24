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

import com.mycila.plugin.Provider;
import com.mycila.plugin.spi.aop.ProxyConfig;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class ProviderProxyFactory {

    private final Binding<?> binding;
    private final Provider<?> provider;

    public ProviderProxyFactory(Binding<?> binding, Provider<?> provider) {
        this.binding = binding;
        this.provider = provider;
    }

    public Object build() {
        if (binding.isProvided()) return provider;
        Class<?> type = binding.getType().getRawType();
        return ProxyConfig.create()
                .withTargetClass(type)
                .withConfigExposed()
                .preferCglib()
                .addInterceptor(new MethodInterceptor() {
                    @Override
                    public Object invoke(MethodInvocation invocation) throws Throwable {
                        return invocation.getMethod().invoke(provider.get(), invocation.getArguments());
                    }
                }).buildProxy();
    }

    public static ProviderProxyFactory create(Binding<?> binding, Provider<?> provider) {
        return new ProviderProxyFactory(binding, provider);
    }

}
