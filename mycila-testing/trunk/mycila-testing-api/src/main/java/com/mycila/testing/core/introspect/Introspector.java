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

import com.mycila.testing.MycilaTestingException;
import static com.mycila.testing.core.api.Ensure.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Handles a test instance
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Introspector {

    private final Object instance;

    public Introspector(Object instance) {
        this.instance = instance;
    }

    /**
     * @return The test instance
     */
    public Object instance() {
        return instance;
    }

    /**
     * @return The test instance's class
     */
    public Class<?> testClass() {
        return instance.getClass();
    }

    /**
     * Select some fields. Use the {@link Filters} class to create and compose filters
     *
     * @param filter Filter to use for selection
     * @return A list containing the selected objects
     */
    public List<Field> selectFields(Filter<? super Field> filter) {
        notNull("Filter", filter);
        List<Field> fields = new ArrayList<Field>();
        for (Class<?> c = testClass(); c != Object.class; c = c.getSuperclass()) {
            for (Field field : c.getDeclaredFields()) {
                if (filter.accept(field)) {
                    fields.add(accessible(field));
                }
            }
        }
        return fields;
    }

    /**
     * Select some methods. Use the {@link Filters} class to create and compose filters
     *
     * @param filter Filter to use for selection
     * @return A list containing the selected objects
     */
    public List<Method> selectMethods(Filter<? super Method> filter) {
        notNull("Filter", filter);
        List<Method> fields = new ArrayList<Method>();
        for (Class<?> c = testClass(); c != Object.class; c = c.getSuperclass()) {
            for (Method method : c.getDeclaredMethods()) {
                if (filter.accept(method)) {
                    fields.add(accessible(method));
                }
            }
        }
        return fields;
    }

    /**
     * Invoke a method on this test instance
     *
     * @param method Method to be invoked
     * @param args   Method argument
     * @return The result of the invokation
     */
    public Object invoke(Method method, Object... args) {
        notNull("Method", method);
        notNull("Method argumentd", args);
        try {
            return method.invoke(instance(), args);
        } catch (IllegalAccessException e) {
            throw new MycilaTestingException(e, "Error invoking method '%s' with arguments: %s: %s", method, Arrays.deepToString(args), e.getMessage());
        } catch (InvocationTargetException e) {
            throw new MycilaTestingException(e.getTargetException().getClass().getSimpleName() + ": " + e.getTargetException().getMessage(), e.getTargetException());
        } catch (Exception e) {
            throw new MycilaTestingException(e, "Error invoking method '%s' with arguments: %s: %s", method, Arrays.deepToString(args), e.getMessage());
        }
    }

    /**
     * Get a field's value
     *
     * @param field The field
     * @return The field value
     */
    public Object get(Field field) {
        notNull("Field", field);
        try {
            return field.get(instance());
        } catch (Exception e) {
            throw new MycilaTestingException(e, "Error getting value of field '%s' on test class '%s': %s", field, testClass().getName(), e.getMessage());
        }
    }

    /**
     * Set a field value
     *
     * @param field The field
     * @param value The new field value
     */
    public void set(Field field, Object value) {
        notNull("Field", field);
        try {
            field.set(instance(), value);
        } catch (Exception e) {
            throw new MycilaTestingException(e, "Error setting value on field '%s' on test class '%s': %s", field, testClass().getName(), e.getMessage());
        }
    }

    public boolean hasAnnotation(Class<? extends Annotation> annot) {
        notNull("Annotation", annot);
        return testClass().isAnnotationPresent(annot);
    }

}
