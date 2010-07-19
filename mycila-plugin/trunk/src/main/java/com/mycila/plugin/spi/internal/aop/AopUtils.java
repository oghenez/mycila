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

package com.mycila.plugin.spi.internal.aop;

import com.mycila.plugin.spi.internal.Assert;

import java.lang.reflect.Proxy;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AopUtils {

    private static final String CGLIB_CLASS_SEPARATOR = "$$";

    /**
     * Check whether the given object is a JDK dynamic proxy or a CGLIB proxy.
     *
     * @param object the object to check
     * @see #isJdkDynamicProxy
     * @see #isCglibProxy
     */
    public static boolean isAopProxy(Object object) {
        return isJdkDynamicProxy(object) || isCglibProxy(object);
    }

    public static boolean isAopProxy(Class<?> c) {
        return isJdkDynamicProxyClass(c) || isCglibProxyClass(c);
    }

    /**
     * Check whether the given object is a JDK dynamic proxy.
     *
     * @param object the object to check
     * @see java.lang.reflect.Proxy#isProxyClass
     */
    public static boolean isJdkDynamicProxy(Object object) {
        return isJdkDynamicProxyClass(object.getClass());
    }

    public static boolean isJdkDynamicProxyClass(Class<?> c) {
        return Proxy.isProxyClass(c);
    }

    /**
     * Check whether the given object is a CGLIB proxy.
     *
     * @param object the object to check
     */
    public static boolean isCglibProxy(Object object) {
        return isCglibProxyClass(object.getClass());
    }

    /**
     * Check whether the specified class is a CGLIB-generated class.
     *
     * @param clazz the class to check
     */
    public static boolean isCglibProxyClass(Class<?> clazz) {
        return (clazz != null && isCglibProxyClassName(clazz.getName()));
    }

    /**
     * Check whether the specified class name is a CGLIB-generated class.
     *
     * @param className the class name to check
     */
    public static boolean isCglibProxyClassName(String className) {
        return (className != null && className.contains(CGLIB_CLASS_SEPARATOR));
    }

    /**
     * Create a composite interface Class for the given interfaces,
     * implementing the given interfaces in one single Class.
     * <p>This implementation builds a JDK proxy class for the given interfaces.
     *
     * @param interfaces  the interfaces to merge
     * @param classLoader the ClassLoader to create the composite Class in
     * @return the merged interface as Class
     * @see java.lang.reflect.Proxy#getProxyClass
     */
    public static Class<?> createCompositeInterface(ClassLoader classLoader, Class<?>... interfaces) {
        Assert.notEmpty(interfaces, "Interfaces must not be empty");
        Assert.notNull(classLoader, "ClassLoader must not be null");
        return Proxy.getProxyClass(classLoader, interfaces);
    }

    /**
     * Determine the target class of the given bean instance which might be an AOP proxy.
     * <p>Returns the target class for an AOP proxy and the plain class else.
     *
     * @param candidate the instance to check (might be an AOP proxy)
     * @return the target class (or the plain class of the given object as fallback;
     *         never <code>null</code>)
     */
    public static Class<?> getTargetClassFromProxy(Object candidate) {
        Assert.notNull(candidate, "Candidate object must not be null");
        if (candidate instanceof ProxyMarker)
            return ((ProxyMarker) candidate).getTargetClass();
        return getTargetClassFromProxyClass(candidate.getClass());
    }

    public static Class<?> getTargetClassFromProxyClass(Class<?> candidate) {
        return isCglibProxyClass(candidate) ? candidate.getSuperclass() : candidate;
    }

}
