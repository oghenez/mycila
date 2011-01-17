/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
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
package com.mycila.testing.core.introspect;

import static com.mycila.testing.core.api.Ensure.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import static java.lang.reflect.Modifier.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Filters {

    private Filters() {
    }

    @SuppressWarnings({"unchecked"})
    public static <T> Filter<T> all() {
        return new Filter<T>() {
            @Override
            public boolean accept(T object) {
                return true;
            }
        };
    }

    @SuppressWarnings({"unchecked"})
    public static <T> Filter<T> none() {
        return new Filter<T>() {
            @Override
            public boolean accept(T object) {
                return false;
            }
        };
    }

    public static <T> Filter<T> not(final Filter<T> filter) {
        notNull("Filter", filter);
        return new Filter<T>() {
            @Override
            public boolean accept(T object) {
                return !filter.accept(object);
            }
        };
    }

    public static <T> Filter<T> and(final Filter<T>... filters) {
        notNull("Filters", filters);
        return new Filter<T>() {
            @Override
            public boolean accept(T object) {
                for (Filter<T> filter : filters) {
                    if (!filter.accept(object)) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    public static <T> Filter<T> or(final Filter<T>... filters) {
        notNull("Filters", filters);
        return new Filter<T>() {
            @Override
            public boolean accept(T object) {
                for (Filter<T> filter : filters) {
                    if (filter.accept(object)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    /**
     * Filter that select fields having specified type or a sub-type of the specified type
     *
     * @param type Selected type
     * @return Created filter
     */
    public static Filter<Field> fieldsProviding(final Class<?> type) {
        notNull("Field type", type);
        return new Filter<Field>() {
            @Override
            public boolean accept(Field object) {
                return type.isAssignableFrom(object.getType());
            }
        };
    }

    /**
     * Filter that select fields having specified type or a super-type of the specified type
     *
     * @param type Selected type
     * @return Created filter
     */
    public static Filter<Field> fieldsAccepting(final Class<?> type) {
        notNull("Field type", type);
        return new Filter<Field>() {
            @Override
            public boolean accept(Field object) {
                return object.getType().isAssignableFrom(type);
            }
        };
    }

    /**
     * Select methods returning objects that are of a given type or of a sub-type of given type
     *
     * @param type The type of the return
     * @return Teh created filter
     */
    public static Filter<Method> methodsReturning(final Class<?> type) {
        notNull("Method return type", type);
        return new Filter<Method>() {
            @Override
            public boolean accept(Method object) {
                return type.isAssignableFrom(object.getReturnType());
            }
        };
    }

    /**
     * Returns fields annotated by a given annotation
     *
     * @param annotation The annotation
     * @return The created filter
     */
    public static Filter<Field> fieldsAnnotatedBy(final Class<? extends Annotation> annotation) {
        notNull("Annotation", annotation);
        return new Filter<Field>() {
            @Override
            public boolean accept(Field object) {
                return object.isAnnotationPresent(annotation);
            }
        };
    }

    /**
     * Returns methods annotated by a given annotation
     *
     * @param annotation The annotation
     * @return The created filter
     */
    public static Filter<Method> methodsAnnotatedBy(final Class<? extends Annotation> annotation) {
        notNull("Annotation", annotation);
        return new Filter<Method>() {
            @Override
            public boolean accept(Method object) {
                return object.isAnnotationPresent(annotation);
            }
        };
    }

    /**
     * This filter will exclude methods that are overriden, as defined
     * in the JLS at http://java.sun.com/docs/books/jls/third_edition/html/classes.html#8.4.8
     *
     * @param methodFilter The method filter used to accept methods
     * @return Created filter
     */
    public static Filter<Method> excludeOverridenMethods(final Filter<Method> methodFilter) {
        return new Filter<Method>() {
            @Override
            public void add(Method element) {
                methodFilter.add(element);
            }

            @Override
            protected boolean accept(Method object) {
                return methodFilter.accept(object);
            }

            @Override
            public List<Method> select() {
                final LinkedList<Method> methods = new LinkedList<Method>(methodFilter.select());
                // first pass to eliminate non overridable methods
                for (Iterator<Method> iterator = methods.iterator(); iterator.hasNext();) {
                    final Method m = iterator.next();
                    final int modifiers = m.getModifiers();
                    if ((modifiers & (FINAL | PRIVATE | STATIC | NATIVE)) != 0) {
                        elements.add(m);
                        iterator.remove();
                    } else if ((modifiers & (INTERFACE | ABSTRACT | VOLATILE)) != 0) {
                        iterator.remove();
                    }
                }
                // second pass to eliminate each remaining overridable methods
                while (!methods.isEmpty()) {
                    // pick a remaining method
                    final Method m1 = methods.poll();
                    boolean overriden = false;
                    // check if it overrides to is overriden against each other ones
                    for (Iterator<Method> iterator = methods.iterator(); iterator.hasNext();) {
                        final Method m2 = iterator.next();
                        if (m1.getName().equals(m2.getName()) && Arrays.equals(m1.getParameterTypes(), m2.getParameterTypes())) {
                            // if the signature is the same, we have to check for override
                            final Class<?> c = m1.getDeclaringClass();
                            final Class<?> a = m2.getDeclaringClass();
                            if (a.isAssignableFrom(c)) {
                                // m1 overrides m2 => remove m2
                                iterator.remove();
                            } else if (c.isAssignableFrom(a)) {
                                // if m2 overrides m1 => stop and pick next method
                                overriden = true;
                                break;
                            }
                        }
                    }
                    if (!overriden) {
                        // we removed all methods m1 overrides, and m1 is not overriden by any other method => we keep it
                        elements.add(m1);
                    }
                }
                return elements;
            }
        };
    }

}
