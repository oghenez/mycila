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

import org.aopalliance.intercept.ConstructorInterceptor;
import org.aopalliance.intercept.MethodInterceptor;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ProxyConfig {

    private final List<Class<?>> ifs = new LinkedList<Class<?>>();
    private Class<?> targetClass;
    private Object target;
    private boolean exposeConfig;
    private MethodInterceptor methodInterceptor;
    private ConstructorInterceptor constructorInterceptor;

    public Object buildProxy() {
        if (exposeConfig)
            addInterface(ProxyElement.class);
        return null;
    }

    public ProxyConfig setTargetClass(Class<?> type) {
        this.targetClass = type;
        return this;
    }

    public ProxyConfig setTarget(Object target) {
        this.target = target;
        return this;
    }

    public ProxyConfig setExposeConfig(boolean b) {
        this.exposeConfig = b;
        return this;
    }

    public ProxyConfig setConstructorInterceptor(ConstructorInterceptor constructorInterceptor) {
        this.constructorInterceptor = constructorInterceptor;
        return this;
    }

    public ProxyConfig setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
        return this;
    }

    public ProxyConfig addInterface(Class<?> c) {
        if (!c.isInterface())
            throw new IllegalArgumentException(c.getName() + " is not an interface");
        ifs.add(c);
        return this;
    }

    public ConstructorInterceptor getConstructorInterceptor() {
        return constructorInterceptor;
    }

    public boolean isExposeConfig() {
        return exposeConfig;
    }

    public List<Class<?>> getInterfaces() {
        return ifs;
    }

    public MethodInterceptor getMethodInterceptor() {
        return methodInterceptor;
    }

    public Class<?> getTargetClass() {
        return targetClass;
    }

    public Object getTarget() {
        return target;
    }

}
