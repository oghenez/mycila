package com.mycila.testing.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class TestInstance {

    private final Object instance;

    TestInstance(Object instance) {
        this.instance = instance;
    }

    public Object getTarget() {
        return instance;
    }

    public Class<?> getTargetClass() {
        return instance.getClass();
    }

    /**
     * Returns all fields declaring the same type or a sub class of given type
     *
     * @param aClass The type of fields to get
     * @return A list of fields where an instance of the given type could be assign
     */
    public Field[] getFieldsOfType(Class<?> aClass) {
        if (aClass == null) {
            return new Field[0];
        }
        List<Field> fields = new ArrayList<Field>();
        Class<?> c = getTargetClass();
        do {
            for (Field field : c.getDeclaredFields()) {
                if (field.getType().isAssignableFrom(aClass)) {
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
    public Field[] getFieldsAnnotatedWith(Class<? extends Annotation> annot) {
        if (annot == null) {
            return new Field[0];
        }
        List<Field> fields = new ArrayList<Field>();
        Class<?> c = getTargetClass();
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
     * List all methods annotated by given annotation.
     *
     * @param annot The annotation
     * @return A list of method having this annotation. Methods not visible are also returned.
     */
    public Method[] getMethodsAnnotatedWith(Class<? extends Annotation> annot) {
        if (annot == null) {
            return new Method[0];
        }
        List<Method> methods = new ArrayList<Method>();
        Class<?> c = getTargetClass();
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

    public Object invoke(Method method, Object... args) {
        try {
            return method.invoke(getTarget(), args);
        } catch (IllegalAccessException e) {
            throw new TestPluginException(e, "Cannot invoke method '%s' on test class '%s'", method, getTargetClass().getName());
        } catch (InvocationTargetException e) {
            throw new TestPluginException(e.getTargetException().getClass().getSimpleName() + ": " + e.getTargetException().getMessage(), e.getTargetException());
        }
    }

    public Object get(Field field) {
        try {
            return field.get(getTarget());
        } catch (IllegalAccessException e) {
            throw new TestPluginException(e, "Cannot get value of field '%s' on test class '%s'", field, getTargetClass().getName());
        }
    }

    public void set(Field field, Object value) {
        try {
            field.set(getTarget(), value);
        } catch (IllegalAccessException e) {
            throw new TestPluginException(e, "Cannot set value on field '%s' on test class '%s'", field, getTargetClass().getName());
        }
    }
}
