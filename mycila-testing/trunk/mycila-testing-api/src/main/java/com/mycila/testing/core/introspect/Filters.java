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

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Filters {

    private static final Filter ALL = new Filter() {
        public boolean accept(Object object) {
            return true;
        }
    };

    private static final Filter NONE = new Filter() {
        public boolean accept(Object object) {
            return true;
        }
    };

    private Filters() {
    }

    @SuppressWarnings({"unchecked"})
    public static <T> Filter<T> all() {
        return ALL;
    }

    @SuppressWarnings({"unchecked"})
    public static <T> Filter<T> none() {
        return NONE;
    }

    public static <T> Filter<T> not(final Filter<T> filter) {
        notNull("Filter", filter);
        return new Filter<T>() {
            public boolean accept(T object) {
                return !filter.accept(object);
            }
        };
    }

    public static <T> Filter<T> and(final Filter<T>... filters) {
        notNull("Filters", filters);
        return new Filter<T>() {
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

    public static Filter<Field> fieldsOfType(final Class<?> type) {
        notNull("Field type", type);
        return new Filter<Field>() {
            public boolean accept(Field object) {
                return type.isAssignableFrom(object.getType());
            }
        };
    }

    public static Filter<Method> methodsOfType(final Class<?> type) {
        notNull("Method return type", type);
        return new Filter<Method>() {
            public boolean accept(Method object) {
                return type.isAssignableFrom(object.getReturnType());
            }
        };
    }

    public static Filter<Field> fieldsAnnotatedBy(final Class<? extends Annotation> annotation) {
        notNull("Annotation", annotation);
        return new Filter<Field>() {
            public boolean accept(Field object) {
                return object.isAnnotationPresent(annotation);
            }
        };
    }

    public static Filter<Method> methodsAnnotatedBy(final Class<? extends Annotation> annotation) {
        notNull("Annotation", annotation);
        return new Filter<Method>() {
            public boolean accept(Method object) {
                return object.isAnnotationPresent(annotation);
            }
        };
    }

}
