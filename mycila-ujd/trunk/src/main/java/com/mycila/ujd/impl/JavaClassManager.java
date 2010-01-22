package com.mycila.ujd.impl;

import com.mycila.ujd.api.JavaClass;

import java.util.WeakHashMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class JavaClassManager {

    private final WeakHashMap<Class<?>, JavaClass<?>> classes = new WeakHashMap<Class<?>, JavaClass<?>>();

    void add(Class<?> aClass) {
        classes.put(aClass, create(aClass));
    }

    Iterable<JavaClass<?>> getJavaClasses() {
        return classes.values();
    }

    private static <T> JavaClass<T> create(Class<T> aClass) {
        return ClassUtils.isGeneratedClass(aClass) ?
                new JavaClassLoadedImpl<T>(aClass) :
                new JavaClassContainedLoadedImpl<T>(aClass);
    }

}
