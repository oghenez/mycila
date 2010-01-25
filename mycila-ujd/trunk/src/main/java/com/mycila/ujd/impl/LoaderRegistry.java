package com.mycila.ujd.impl;

import com.mycila.ujd.api.JavaClass;
import com.mycila.ujd.api.Loader;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class LoaderRegistry {

    private final ConcurrentHashMap<ClassLoader, LoaderImpl> loaders = new ConcurrentHashMap<ClassLoader, LoaderImpl>();
    private final JVMImpl jvm;

    LoaderRegistry(JVMImpl jvm) {
        this.jvm = jvm;
    }

    Loader get(ClassLoader classLoader) {
        if (classLoader == null)
            classLoader = ClassLoader.getSystemClassLoader().getParent();
        if (classLoader == null)
            throw new AssertionError("Error locating bootstrap classloader !");
        LoaderImpl loader = loaders.get(classLoader);
        if (loader == null)
            loaders.put(classLoader, loader = new LoaderImpl(jvm, classLoader));
        return loader;
    }

    Loader get(JavaClass<?> theClass) {
        return get(theClass.get().getClassLoader());
    }

}