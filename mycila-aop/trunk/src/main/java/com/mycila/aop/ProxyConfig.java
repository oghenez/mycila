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

package com.mycila.aop;

import com.google.common.collect.Iterables;
import com.mycila.aop.internal.CglibProxyConstruction;
import com.mycila.aop.internal.JdkProxyConstruction;
import org.aopalliance.intercept.ConstructorInterceptor;
import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ProxyConfig {

    private static final boolean cglibAvailable = !Boolean.getBoolean("mycila.aop.cglib.disabled");
    private static final String CGLIB_CLASS_SEPARATOR = "$$";

    private final Collection<Class<?>> ifs = new LinkedHashSet<Class<?>>(2);
    private final List<ConstructorInterceptor> constructorInterceptors = new ArrayList<ConstructorInterceptor>(2);
    private final List<MethodInterceptor> methodInterceptors = new ArrayList<MethodInterceptor>(2);
    private Class<?> targetClass;
    private Object target;
    private ProxyConstruction proxy;
    private boolean configExposed;
    private ClassLoader classLoader;
    private boolean cglibPrefered;

    private ProxyConfig() {
    }

    /* getters */

    public List<MethodInterceptor> getMethodInterceptors() {
        return Collections.unmodifiableList(methodInterceptors);
    }

    public List<ConstructorInterceptor> getConstructorInterceptors() {
        return Collections.unmodifiableList(constructorInterceptors);
    }

    public boolean isConfigExposed() {
        return configExposed;
    }

    public boolean isCglibPrefered() {
        return cglibPrefered;
    }

    public Class<?>[] getInterfaces() {
        return ifs.toArray(new Class<?>[ifs.size()]);
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

    public ProxyConstruction getProxy() {
        return proxy;
    }

    /* static method ctor */

    public static Builder create() {
        return new Builder();
    }

    /* build proxy here */

    private void buildProxy() {
        // normalize targetClass
        if (targetClass != null && targetClass.isInterface()) {
            ifs.add(targetClass);
            if (classLoader == null)
                classLoader = targetClass.getClassLoader();
            targetClass = null;
        }
        if (targetClass == null && target != null)
            targetClass = target.getClass();
        while (targetClass != null && targetClass.getName().contains(CGLIB_CLASS_SEPARATOR))
            targetClass = targetClass.getSuperclass();
        // complete interfaces
        if (target != null)
            ifs.addAll(Arrays.asList(target.getClass().getInterfaces()));
        if (configExposed)
            ifs.add(ProxyElement.class);
        // determine classloader
        if (classLoader == null && targetClass != null)
            classLoader = targetClass.getClassLoader();
        if (classLoader == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = getClass().getClassLoader();
                if (classLoader == null)
                    classLoader = ClassLoader.getSystemClassLoader();
            }
        }
        // if target class still null, create one composite interface
        if (targetClass == null)
            targetClass = Proxy.getProxyClass(classLoader, ifs.toArray(new Class<?>[ifs.size()]));
        // set target
        if (target == null)
            target = new Object() {
                @Override
                public String toString() {
                    return "Proxy of " + targetClass.getName();
                }
            };
        // build proxifier
        proxy = determineCreator();
    }

    private ProxyConstruction determineCreator() {
        if (targetClass.isInterface())
            return cglibPrefered && cglibAvailable ? new CglibProxyConstruction(this) : new JdkProxyConstruction(this);
        if (!cglibAvailable)
            throw new AssertionError("CGLIB required but not found on your classpath");
        return new CglibProxyConstruction(this);
    }

    /* builder */

    public static class Builder {

        ProxyConfig config = new ProxyConfig();

        private Builder() {
        }

        public Builder withTargetClass(Class<?> type) {
            config.targetClass = type;
            return this;
        }

        public Builder withTarget(Object target) {
            config.target = target;
            return this;
        }

        public Builder withClassLoader(ClassLoader classLoader) {
            config.classLoader = classLoader;
            return this;
        }

        public Builder withConfigExposed() {
            config.configExposed = true;
            return this;
        }

        public Builder preferCglib() {
            config.cglibPrefered = true;
            return this;
        }

        public Builder addMethodInterceptor(MethodInterceptor methodInterceptor) {
            config.methodInterceptors.add(methodInterceptor);
            return this;
        }

        public Builder addMethodInterceptors(MethodInterceptor... methodInterceptors) {
            return addMethodInterceptors(Arrays.asList(methodInterceptors));
        }

        public Builder addMethodInterceptors(Iterable<? extends MethodInterceptor> methodInterceptors) {
            Iterables.addAll(config.methodInterceptors, methodInterceptors);
            return this;
        }

        public Builder addConstructorInterceptor(ConstructorInterceptor constructorInterceptor) {
            config.constructorInterceptors.add(constructorInterceptor);
            return this;
        }

        public Builder addConstructorInterceptors(ConstructorInterceptor... constructorInterceptors) {
            return addConstructorInterceptors(Arrays.asList(constructorInterceptors));
        }

        public Builder addConstructorInterceptors(Iterable<? extends ConstructorInterceptor> constructorInterceptors) {
            Iterables.addAll(config.constructorInterceptors, constructorInterceptors);
            return this;
        }

        public Builder addInterface(Class<?> c) {
            if (!c.isInterface())
                throw new IllegalArgumentException(c.getName() + " is not an interface");
            config.ifs.add(c);
            return this;
        }

        public Builder addInterfaces(Class<?>... interfaces) {
            for (Class<?> ife : interfaces)
                addInterface(ife);
            return this;
        }

        public Builder addInterfaces(Iterable<? extends Class<?>> interfaces) {
            for (Class<?> ife : interfaces)
                addInterface(ife);
            return this;
        }

        public ProxyConstruction buildProxy() {
            if (config.proxy == null)
                config.buildProxy();
            return config.getProxy();
        }
    }
}
