/**
 * Copyright (C) 2010 Mycila <mathieu.carbou@gmail.com>
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

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;
import com.google.inject.matcher.Matchers;
import com.mycila.inject.internal.WeakCache;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Members {

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

    public static Iterable<Method> findAnnotatedMethods(Class<?> type, Class<? extends Annotation> annotationType) {
        return findMethods(type, Matchers.annotatedWith(annotationType));
    }

    public static Iterable<Method> findMethods(final Class<?> clazz, final Matcher<? super Method> matcher) {
        return Iterables.filter(METHOD_CACHE.get(clazz), new Predicate<Method>() {
            @Override
            public boolean apply(Method input) {
                return matcher.matches(input);
            }
        });
    }

    public static Iterable<Field> findAnnotatedFields(Class<?> type, Class<? extends Annotation> annotationType) {
        List<Field> fields = new LinkedList<Field>();
        while (type != null && type != Object.class) {
            for (Field field : type.getDeclaredFields())
                if (field.isAnnotationPresent(annotationType))
                    fields.add(field);
            type = type.getSuperclass();
        }
        return fields;
    }

    private Members() {
    }

    //TODO: optimize - memoize
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