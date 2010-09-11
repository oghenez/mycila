/**
 * Copyright (C) 2010 mycila.com <mathieu.carbou@gmail.com>
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

package com.mycila.inject.util;

import com.google.inject.internal.cglib.core.ReflectUtils;
import com.google.inject.internal.cglib.core.Signature;
import com.google.inject.internal.util.AbstractIterator;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Methods {

    public static final Matcher<Method> METHOD_WITHOUT_PARAMETER = new AbstractMatcher<Method>() {
        @Override
        public boolean matches(Method o) {
            return o.getParameterTypes().length == 0;
        }
    };

    public static Matcher<Method> withParameterTypes(final Class<?>... classes) {
        return new AbstractMatcher<Method>() {
            @Override
            public boolean matches(Method m) {
                Class<?>[] thisParams = m.getParameterTypes();
                if (thisParams.length != classes.length)
                    return false;
                int c = 0;
                for (Class<?> thisParam : thisParams)
                    if (thisParam != classes[c++])
                        return false;
                return true;
            }
        };
    }

    public static Matcher<Method> named(final String methodName) {
        return new AbstractMatcher<Method>() {
            @Override
            public boolean matches(Method method) {
                return method.getName().equals(methodName);
            }
        };
    }

    public static Iterable<Method> listAll(final Class<?> clazz, final Matcher<? super Method> matcher) {
        return new Iterable<Method>() {
            @Override
            public Iterator<Method> iterator() {
                final Iterator<Method> unfiltered = METHOD_CACHE.get(clazz).iterator();
                return new AbstractIterator<Method>() {
                    @Override
                    protected Method computeNext() {
                        while (unfiltered.hasNext()) {
                            Method element = unfiltered.next();
                            if (matcher.matches(element)) {
                                return element;
                            }
                        }
                        return endOfData();
                    }
                };
            }
        };
    }

    private Methods() {
    }

    private static final WeakCache<Class<?>, List<Method>> METHOD_CACHE = new WeakCache<Class<?>, List<Method>>(new WeakCache.Provider<Class<?>, List<Method>>() {
        @Override
        public List<Method> get(Class<?> clazz) {
            List<Method> methods = new LinkedList<Method>();
            Map<Signature, Method> signatureMethod = new LinkedHashMap<Signature, Method>();
            while (clazz != null && clazz != Object.class) {
                for (Method method : clazz.isInterface() ? clazz.getMethods() : clazz.getDeclaredMethods()) {
                    if (method.isSynthetic() || method.isBridge())
                        continue;
                    Signature thisSignature = ReflectUtils.getSignature(method);
                    Method existing = signatureMethod.get(thisSignature);
                    if (existing == null) {
                        signatureMethod.put(thisSignature, method);
                        methods.add(method);
                    } else if (!isOverridable(existing, clazz)) {
                        methods.add(method);
                    }
                }
                clazz = clazz.getSuperclass();
            }
            return methods;
        }
    });

    private static boolean isOverridable(Method method, Class targetClass) {
        return !Modifier.isPrivate(method.getModifiers())
                && (Modifier.isPublic(method.getModifiers())
                || Modifier.isProtected(method.getModifiers())
                || getPackageName(method.getDeclaringClass()).equals(getPackageName(targetClass)));
    }

    private static String getPackageName(Class<?> clazz) {
        String className = clazz.getName();
        int lastDotIndex = className.lastIndexOf('.');
        return lastDotIndex != -1 ? className.substring(0, lastDotIndex) : "";
    }

}