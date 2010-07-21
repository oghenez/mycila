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

import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ProxyConfig {

    private final List<Class<?>> ifs = new LinkedList<Class<?>>();
    private Class<?> targetClass;
    private Object target;
    private Object proxy;
    private boolean configExposed;
    private MethodInterceptor methodInterceptor;
    private ClassLoader classLoader;

    /* build proxy here */

    public Object getProxy() {
        if (proxy != null)
            return proxy;
        if (targetClass.isInterface()) {
            ifs.add(0, targetClass);
            targetClass = null;
        }
        if (configExposed)
            addInterface(ProxyElement.class);
        if(targetClass == null)
            targetClass = Proxy.getProxyClass(classLoader, ifs.toArray(new Class<?>[ifs.size()]));
        
        return proxy = targetClass.isInterface() ?
                new JdkProxyCreator(this).buildProxy() :
                new CglibProxyCreator(this).buildProxy();
    }

    /* builders */

    public ProxyConfig withTargetClass(Class<?> type) {
        this.targetClass = type;
        return this;
    }

    public ProxyConfig withTarget(Object target) {
        this.target = target;
        return this;
    }

    public ProxyConfig withClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
        return this;
    }

    public ProxyConfig withConfigExposed() {
        this.configExposed = true;
        return this;
    }

    public ProxyConfig withInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
        return this;
    }

    public ProxyConfig addInterface(Class<?> c) {
        if (!c.isInterface())
            throw new IllegalArgumentException(c.getName() + " is not an interface");
        ifs.add(c);
        return this;
    }

    public ProxyConfig addInterfaces(Class<?>... interfaces) {
        for (Class<?> ife : interfaces)
            addInterface(ife);
        return this;
    }

    public ProxyConfig addInterfaces(Iterable<Class<?>> interfaces) {
        for (Class<?> ife : interfaces)
            addInterface(ife);
        return this;
    }

    /* getters */

    public MethodInterceptor getMethodInterceptor() {
        return methodInterceptor;
    }

    public boolean isConfigExposed() {
        return configExposed;
    }

    public List<Class<?>> getInterfaces() {
        return Collections.unmodifiableList(ifs);
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Object getTarget() {
        return target;
    }

    public ClassLoader getClassLoader() {
        return classLoader;
    }
}
