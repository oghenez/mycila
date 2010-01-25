package com.mycila.ujd.impl;

import com.mycila.ujd.api.JavaClass;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class JavaClassRegistry {

    private final ConcurrentHashMap<Class<?>, JavaClassLoadedImpl<?>> classes = new ConcurrentHashMap<Class<?>, JavaClassLoadedImpl<?>>();
    private final JVMImpl jvm;

    JavaClassRegistry(JVMImpl jvm) {
        this.jvm = jvm;
    }

    <T> JavaClass<T> add(Class<T> aClass) {
        JavaClassLoadedImpl<?> jc = classes.get(aClass);
        if (jc == null)
            classes.put(aClass, jc = create(aClass));
        return (JavaClass<T>) jc;
    }

    Iterable<? extends JavaClass<?>> getJavaClasses() {
        return classes.values();
    }

    private <T> JavaClassLoadedImpl<T> create(Class<T> aClass) {
        return ClassUtils.isGeneratedClass(aClass) ?
                new JavaClassLoadedImpl<T>(jvm, aClass) :
                new JavaClassContainedLoadedImpl<T>(jvm, aClass);
    }
    
}
