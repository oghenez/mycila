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

package com.mycila.plugin.spi.aop;

import net.sf.cglib.proxy.Enhancer;

import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class CglibProxyCreator implements ProxyCreator, ProxyElement {
    private final ProxyConfig proxyConfig;

    CglibProxyCreator(ProxyConfig proxyConfig) {
        this.proxyConfig = proxyConfig;
    }

    @Override
    public ProxyConfig getProxyConfig() {
        return proxyConfig;
    }

    @Override
    public ProxyConstructor getProxyConstructor(final Class<?>... parameterTypes) {
        return new ProxyConstructor() {
            @Override
            public Object newProxyInstance(Object... arguments) {
                Enhancer enhancer = CglibUtils.newEnhancer(proxyConfig.getTargetClass());
                enhancer.setInterfaces(proxyConfig.getInterfaces());
                
                return enhancer.create(parameterTypes, arguments);
            }
        };
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // method call on proxy to request proxy info
        if (proxyConfig.isConfigExposed()
                && method.getDeclaringClass().isInterface()
                && method.getDeclaringClass().isAssignableFrom(ProxyElement.class))
            return method.invoke(this, args);
        // go through interceptors
        Object ret = new ReflectiveMethodInvocation(proxy, proxyConfig.getTarget(), method, args, proxyConfig.getMethodInterceptors()).proceed();
        // Special case: it returned "this" and the return type of the method
        // is type-compatible. Note that we can't help if the target sets
        // a reference to itself in another returned object.
        return ret != null && ret == proxyConfig.getTarget() && method.getReturnType().isInstance(proxy) ? proxy : ret;
    }

}