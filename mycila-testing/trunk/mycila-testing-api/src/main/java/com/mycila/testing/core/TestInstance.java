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
package com.mycila.testing.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
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
public final class TestInstance {

    private final Object instance;

    public TestInstance(Object instance) {
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
     * Returns all fields declaring the same type or a sub class of given type
     *
     * @param type The type of fields to get
     * @return A list of fields where an instance of the given type could be assign
     */
    public Field[] findFieldsOfType(Class<?> type) {
        notNull(type, "Type cannot be null");
        List<Field> fields = new ArrayList<Field>();
        Class<?> c = testClass();
        do {
            for (Field field : c.getDeclaredFields()) {
                if (field.getType().isAssignableFrom(type)) {
                    fields.add(accessible(field));
                }
            }
            c = c.getSuperclass();
        }
        while (c != null);
        return fields.toArray(new Field[fields.size()]);
    }

    /**
     * Returns all fields annotated with the given annotation.
     *
     * @param annot Annotation on fields
     * @return A list of all fields annotated with the annotation, even non visible ones.
     */
    public Field[] findFieldsAnnotatedWith(Class<? extends Annotation> annot) {
        notNull(annot, "Annotation cannot be null");
        List<Field> fields = new ArrayList<Field>();
        Class<?> c = testClass();
        do {
            for (Field field : c.getDeclaredFields()) {
                if (field.isAnnotationPresent(annot)) {
                    fields.add(accessible(field));
                }
            }
            c = c.getSuperclass();
        }
        while (c != null);
        return fields.toArray(new Field[fields.size()]);
    }

    /**
     * Returns all fields annotated with the given annotation having the given type
     *
     * @param type  Type of field
     * @param annot Annotation on fields
     * @return A list of all fields annotated with the annotation, even non visible ones.
     */
    public Field[] findFieldsOfTypeAnnotatedWith(Class<?> type, Class<? extends Annotation> annot) {
        notNull(annot, "Annotation cannot be null");
        notNull(type, "Type cannot be null");
        List<Field> fields = new ArrayList<Field>();
        Class<?> c = testClass();
        do {
            for (Field field : c.getDeclaredFields()) {
                if (field.getType().isAssignableFrom(type) && field.isAnnotationPresent(annot)) {
                    fields.add(accessible(field));
                }
            }
            c = c.getSuperclass();
        }
        while (c != null);
        return fields.toArray(new Field[fields.size()]);
    }

    /**
     * List all methods annotated by given annotation.
     *
     * @param type The method return type
     * @return A list of method having this return type. Methods not visible are also returned.
     */
    public Method[] findMethodsOfType(Class<?> type) {
        notNull(type, "Return type cannot be null");
        List<Method> methods = new ArrayList<Method>();
        Class<?> c = testClass();
        do {
            for (Method method : c.getDeclaredMethods()) {
                if (type.isAssignableFrom(method.getReturnType())) {
                    methods.add(accessible(method));
                }
            }
            c = c.getSuperclass();
        }
        while (c != null);
        return methods.toArray(new Method[methods.size()]);
    }

    /**
     * List all methods annotated by given annotation.
     *
     * @param annot The annotation
     * @return A list of method having this annotation. Methods not visible are also returned.
     */
    public Method[] findMethodsAnnotatedWith(Class<? extends Annotation> annot) {
        notNull(annot, "Annotation cannot be null");
        List<Method> methods = new ArrayList<Method>();
        Class<?> c = testClass();
        do {
            for (Method method : c.getDeclaredMethods()) {
                if (method.isAnnotationPresent(annot)) {
                    methods.add(accessible(method));
                }
            }
            c = c.getSuperclass();
        }
        while (c != null);
        return methods.toArray(new Method[methods.size()]);
    }

    private <T extends AccessibleObject> T accessible(T a) {
        if (!a.isAccessible()) {
            a.setAccessible(true);
        }
        return a;
    }

    /**
     * Returns all methods annotated with the given annotation and returning the given type
     *
     * @param type  Type of method's return
     * @param annot Annotation on methods
     * @return A list of all methods annotated with the annotation, even non visible ones.
     */
    public Method[] findMethodsOfTypeAnnotatedWith(Class<?> type, Class<? extends Annotation> annot) {
        notNull(type, "Return type cannot be null");
        notNull(annot, "Annotation cannot be null");
        List<Method> methods = new ArrayList<Method>();
        Class<?> c = testClass();
        do {
            for (Method method : c.getDeclaredMethods()) {
                if (type.isAssignableFrom(method.getReturnType()) && method.isAnnotationPresent(annot)) {
                    methods.add(accessible(method));
                }
            }
            c = c.getSuperclass();
        }
        while (c != null);
        return methods.toArray(new Method[methods.size()]);
    }

    public Object invoke(Method method, Object... args) {
        try {
            return method.invoke(instance(), args);
        } catch (IllegalAccessException e) {
            throw new TestPluginException(e, "Error invoking method '%s' with arguments: %s: %s", method, Arrays.deepToString(args), e.getMessage());
        } catch (InvocationTargetException e) {
            throw new TestPluginException(e.getTargetException().getClass().getSimpleName() + ": " + e.getTargetException().getMessage(), e.getTargetException());
        } catch (Exception e) {
            throw new TestPluginException(e, "Error invoking method '%s' with arguments: %s: %s", method, Arrays.deepToString(args), e.getMessage());
        }
    }

    public Object get(Field field) {
        try {
            return field.get(instance());
        } catch (Exception e) {
            throw new TestPluginException(e, "Error getting value of field '%s' on test class '%s': %s", field, testClass().getName(), e.getMessage());
        }
    }

    public void set(Field field, Object value) {
        try {
            field.set(instance(), value);
        } catch (Exception e) {
            throw new TestPluginException(e, "Error setting value on field '%s' on test class '%s': %s", field, testClass().getName(), e.getMessage());
        }
    }

    private void notNull(Object o, String message) {
        if (o == null) {
            throw new IllegalArgumentException(message);
        }
    }

}
