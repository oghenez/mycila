package com.ovea.acidmelon.agent.testing;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AnnotationValueProvider<T> implements Iterable<T> {

    private final List<T> features = new ArrayList<T>();
    private final Class[] values;
    private final Class<T> type;
    private boolean loaded;

    public AnnotationValueProvider(Class<?> targetClass, Class<? extends Annotation> annotClass, Class<T> type) {
        this.type = type;
        Annotation annot = targetClass.getAnnotation(annotClass);
        if (annot != null)
            try {
                Method value = annotClass.getMethod("value");
                Class<?> t = value.getReturnType().isArray() ? value.getReturnType().getComponentType() : value.getReturnType();
                if (!Class.class.isAssignableFrom(t))
                    throw new IllegalStateException("Expecting Class or Class[] for return type but the method is: " + value);
                values = value.getReturnType().isArray() ?
                        Class[].class.cast(value.invoke(annot)) :
                        new Class[]{Class.class.cast(value.invoke(annot))};
            } catch (NoSuchMethodException e) {
                throw new IllegalStateException("No value() method in annotation " + annotClass.getName(), e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e.getMessage(), e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        else values = null;
    }

    public boolean isEmpty() {
        return values == null || values.length == 0;
    }

    @Override
    public Iterator<T> iterator() {
        try {
            return loadFeatures().iterator();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable.getMessage(), throwable);
        }
    }

    public synchronized List<T> loadFeatures() throws Throwable {
        if (loaded) return features;
        if (!isEmpty())
            for (Class featureClass : values)
                try {
                    Constructor ctor = featureClass.getDeclaredConstructor();
                    ctor.setAccessible(true);
                    features.add(type.cast(ctor.newInstance()));
                } catch (InvocationTargetException e) {
                    throw e.getTargetException();
                }
        loaded = true;
        return features;
    }
}
