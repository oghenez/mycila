package com.mycila.ujd.impl;

import com.mycila.ujd.api.JavaClass;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class JavaClassRegistry {

    private final ConcurrentHashMap<Class<?>, JavaClassImpl<?>> classes = new ConcurrentHashMap<Class<?>, JavaClassImpl<?>>();
    private final JVMImpl jvm;

    JavaClassRegistry(JVMImpl jvm) {
        this.jvm = jvm;
    }

    <T> JavaClass<T> add(Class<T> aClass) {
        JavaClassImpl<?> jc = classes.get(aClass);
        if (jc == null)
            classes.put(aClass, jc = create(aClass));
        return (JavaClass<T>) jc;
    }

    Iterable<? extends JavaClass<?>> getJavaClasses() {
        return classes.values();
    }

    private <T> JavaClassImpl<T> create(Class<T> aClass) {
        return ClassUtils.isGeneratedClass(aClass) ?
                new JavaClassImpl<T>(jvm, aClass) :
                new ContainedJavaClassImpl<T>(jvm, aClass);
    }

}
