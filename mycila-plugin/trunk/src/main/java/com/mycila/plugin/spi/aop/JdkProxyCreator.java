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

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class JdkProxyCreator implements ProxyCreator, ProxyElement, InvocationHandler, ProxyConstructor {
    private final ProxyConfig proxyConfig;
    private boolean equalsDefined;

    JdkProxyCreator(ProxyConfig proxyConfig) {
        this.proxyConfig = proxyConfig;
    }

    @Override
    public ProxyConfig getProxyConfig() {
        return proxyConfig;
    }

    @Override
    public ProxyConstructor getProxyConstructor(Class<?>... parameterTypes) {
        return this;
    }

    @Override
    public Object newProxyInstance(Object... arguments) {
        for (Method method : proxyConfig.getTargetClass().getMethods()) {
            if (isEquals(method)) {
                equalsDefined = true;
                break;
            }
        }
        return Proxy.newProxyInstance(
                proxyConfig.getClassLoader(),
                proxyConfig.getInterfaces(),
                this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // check equals
        if (!equalsDefined && isEquals(method))
            return equals(args[0]);
        // method call on proxy to request proxy info
        if (proxyConfig.isConfigExposed()
                && method.getDeclaringClass().isInterface()
                && method.getDeclaringClass().isAssignableFrom(ProxyElement.class))
            return method.invoke(this, args);
        // go through interceptors
        MethodInvocation invocation = new ReflectiveMethodInvocation(
                proxy,
                proxyConfig.getTarget(),
                method,
                args,
                proxyConfig.getMethodInterceptors());
        Object ret = invocation.proceed();
        // Special case: it returned "this" and the return type of the method
        // is type-compatible. Note that we can't help if the target sets
        // a reference to itself in another returned object.
        return ret != null && ret == proxyConfig.getTarget() && method.getReturnType().isInstance(proxy) ? proxy : ret;
    }

    /**
     * Equality means interfaces, advisors and TargetSource are equal.
     * <p>The compared object may be a JdkDynamicAopProxy instance itself
     * or a dynamic proxy wrapping a JdkDynamicAopProxy instance.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        ProxyConfig otherConfig;
        if (other instanceof ProxyElement) {
            otherConfig = ((ProxyElement) other).getProxyConfig();
        } else if (Proxy.isProxyClass(other.getClass())) {
            Object ih = Proxy.getInvocationHandler(other);
            if (!(ih instanceof ProxyElement)) return false;
            otherConfig = ((ProxyElement) ih).getProxyConfig();
        } else return false;
        // If we get here, otherProxy is the other AopProxy.
        return proxyConfig == otherConfig
                || proxyConfig.getTarget().equals(otherConfig.getTarget());
    }

    private boolean isEquals(Method method) {
        return method.getName().equals("equals")
                && method.getParameterTypes().length == 1
                && method.getParameterTypes()[0] == Object.class;
    }
}
