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

    private static final boolean cglibAvailable;

    static {
        cglibAvailable = hasCGLIB(ProxyConfig.class.getClassLoader());
    }

    private static boolean hasCGLIB(ClassLoader classLoader) {
        try {
            classLoader.loadClass("net.sf.cglib.proxy.Callback");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    private static final String CGLIB_CLASS_SEPARATOR = "$$";

    private final Collection<Class<?>> ifs = new LinkedHashSet<Class<?>>(2);
    private final List<MethodInterceptor> methodInterceptors = new ArrayList<MethodInterceptor>(1);
    private Class<?> targetClass;
    private Object target;
    private Object proxy;
    private boolean configExposed;
    private ClassLoader classLoader;
    private boolean cglibPrefered;
    private Class<?>[] constructionParameterTypes = new Class<?>[0];
    private Object[] constructionArguments = new Object[0];

    private ProxyConfig() {
    }

    /* getters */

    public List<MethodInterceptor> getMethodInterceptors() {
        return Collections.unmodifiableList(methodInterceptors);
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

    public Object getProxy() {
        return proxy;
    }

    public Object[] getConstructionArguments() {
        return constructionArguments;
    }

    public Class<?>[] getConstructionParameterTypes() {
        return constructionParameterTypes;
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
        if (targetClass != null && targetClass.getName().contains(CGLIB_CLASS_SEPARATOR))
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
        // build proxy
        ProxyCreator creator = determineCreator();
        proxy = creator.getProxyConstructor(constructionParameterTypes).newProxyInstance(constructionArguments);
    }

    private ProxyCreator determineCreator() {
        if (targetClass.isInterface())
            return cglibPrefered && cglibAvailable ? new CglibProxyCreator(this) : new JdkProxyCreator(this);
        if (!cglibAvailable)
            throw new AssertionError("CGLIB required but not found on your classpath");
        return new CglibProxyCreator(this);
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

        public Builder withConstructionParameterTypes(Class<?>... parameterTypes) {
            config.constructionParameterTypes = parameterTypes;
            return this;
        }

        public Builder withConstructionArguments(Object... args) {
            config.constructionArguments = args;
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

        public Builder addInterceptor(MethodInterceptor methodInterceptor) {
            config.methodInterceptors.add(methodInterceptor);
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

        public Builder addInterfaces(Iterable<Class<?>> interfaces) {
            for (Class<?> ife : interfaces)
                addInterface(ife);
            return this;
        }

        public Object buildProxy() {
            config.buildProxy();
            return config.getProxy();
        }
    }
}
