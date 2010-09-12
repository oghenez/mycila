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

package com.mycila.inject.internal;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class CglibUtils {

    private CglibUtils() {
    }

    private static final ClassLoader CGLIB_CLASS_LOADER = net.sf.cglib.proxy.Enhancer.class.getClassLoader();
    private static final String CGLIB_PACKAGE = net.sf.cglib.proxy.Enhancer.class.getName().replaceFirst("\\.cglib\\..*$", ".cglib");

    private static final WeakCache<ClassLoader, ClassLoader> CLASS_LOADER_CACHE = new WeakCache<ClassLoader, ClassLoader>(new WeakCache.Provider<ClassLoader, ClassLoader>() {
        @Override
        public ClassLoader get(ClassLoader key) {
            return new BridgeClassLoader(key, CGLIB_CLASS_LOADER, CGLIB_PACKAGE);
        }
    });

    private static final net.sf.cglib.core.NamingPolicy NAMING_POLICY = new net.sf.cglib.core.DefaultNamingPolicy() {
        @Override
        protected String getTag() {
            return "ByMycilaGuice";
        }
    };

    private static final WeakCache<Class<?>, net.sf.cglib.reflect.FastClass> FAST_CLASS_CACHE
            = new WeakCache<Class<?>, net.sf.cglib.reflect.FastClass>(
            new WeakCache.Provider<Class<?>, net.sf.cglib.reflect.FastClass>() {
                @Override
                public net.sf.cglib.reflect.FastClass get(Class<?> type) {
                    net.sf.cglib.reflect.FastClass.Generator generator = new net.sf.cglib.reflect.FastClass.Generator();
                    generator.setType(type);
                    generator.setClassLoader(getClassLoader(type));
                    generator.setNamingPolicy(NAMING_POLICY);
                    return generator.create();
                }
            });

    private static ClassLoader getClassLoader(Class<?> type) {
        ClassLoader delegate = canonicalize(type.getClassLoader());
        if (delegate == getSystemClassLoader())
            return delegate;
        if (delegate instanceof BridgeClassLoader)
            return delegate;
        return CLASS_LOADER_CACHE.get(delegate);
    }

    public static net.sf.cglib.reflect.FastClass getFastClass(Class<?> c) {
        return FAST_CLASS_CACHE.get(c);
    }

    static net.sf.cglib.proxy.Enhancer newEnhancer(Class<?> type) {
        net.sf.cglib.proxy.Enhancer enhancer = new net.sf.cglib.proxy.Enhancer();
        enhancer.setSuperclass(type);
        enhancer.setUseFactory(false);
        enhancer.setClassLoader(getClassLoader(type));
        enhancer.setNamingPolicy(NAMING_POLICY);
        enhancer.setStrategy(new net.sf.cglib.transform.impl.UndeclaredThrowableStrategy(UndeclaredThrowableException.class));
        enhancer.setInterceptDuringConstruction(false);
        return enhancer;
    }

    private static ClassLoader canonicalize(ClassLoader classLoader) {
        return classLoader != null
                ? classLoader
                : getSystemClassLoader();
    }

    /**
     * Returns the system classloader, or {@code null} if we don't have
     * permission.
     */
    private static ClassLoader getSystemClassLoader() {
        try {
            return ClassLoader.getSystemClassLoader();
        } catch (SecurityException e) {
            throw new AssertionError("Cannot get System Classloader !");
        }
    }

}
