/**
 * Copyright (C) 2009 Mathieu Carbou <mathieu.carbou@gmail.com>
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

package com.mycila.event.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ClassUtils {

    private ClassUtils() {
    }

    public static Iterable<Method> filterAnnotatedMethods(final Iterable<Method> iterable, final Class<? extends Annotation>... allowedAnnotations) {
        return filterAnnotatedMethods(iterable, Arrays.asList(allowedAnnotations));
    }

    public static Iterable<Method> filterAnnotatedMethods(final Iterable<Method> iterable, final Iterable<Class<? extends Annotation>> allowedAnnotations) {
        return new Iterable<Method>() {
            public Iterator<Method> iterator() {
                final Iterator<Method> methodIterator = iterable.iterator();
                return new FilterIterator<Method, Method>(methodIterator) {
                    @Override
                    protected Method filter(Method delegate) {
                        for (Class<? extends Annotation> annotation : allowedAnnotations)
                            if (delegate.isAnnotationPresent(annotation))
                                return delegate;
                        return null;
                    }
                };
            }
        };
    }

    public static Iterable<Method> getAllDeclaredMethods(Class<?> clazz) {
        List<Class<?>> hierarchy = new ArrayList<Class<?>>();
        while (clazz != null && clazz != Object.class) {
            hierarchy.add(clazz);
            clazz = clazz.getSuperclass();
        }
        LinkedHashMap<MethodSignature, Method> all = new LinkedHashMap<MethodSignature, Method>();
        for (Class<?> c : hierarchy) {
            Method[] methods = c.getDeclaredMethods();
            for (Method method : methods) {
                MethodSignature signature = MethodSignature.of(method);
                if (!all.containsKey(signature))
                    all.put(signature, method);
            }
        }
        return new LinkedList<Method>(all.values());
    }

}