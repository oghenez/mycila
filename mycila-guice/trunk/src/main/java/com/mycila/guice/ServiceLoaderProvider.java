package com.mycila.guice;

import com.google.inject.Inject;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.mycila.util.ServiceClassLoader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ServiceLoaderProvider<T> implements Provider<T[]> {

    private final Class<T> type;

    @Inject
    com.google.inject.Injector injector;

    Key<? extends ClassLoader> classLoaderKey;

    public ServiceLoaderProvider(Class<T> type) {
        this.type = type;
    }

    public ServiceLoaderProvider<T> withClassLoader(Class<? extends ClassLoader> classLoaderType) {
        return withClassLoader(Key.get(classLoaderType));
    }

    public ServiceLoaderProvider<T> withClassLoader(Class<? extends ClassLoader> classLoaderType, Class<? extends Annotation> annot) {
        return withClassLoader(Key.get(classLoaderType, annot));
    }

    public ServiceLoaderProvider<T> withClassLoader(Class<? extends ClassLoader> classLoaderType, Annotation annot) {
        return withClassLoader(Key.get(classLoaderType, annot));
    }

    public ServiceLoaderProvider<T> withClassLoader(Key<? extends ClassLoader> key) {
        this.classLoaderKey = key;
        return this;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public T[] get() {
        List<T> instances = new ArrayList<T>();
        ServiceClassLoader<T> loader = classLoaderKey == null ?
                ServiceClassLoader.load(type, Thread.currentThread().getContextClassLoader()) :
                ServiceClassLoader.load(type, injector.getInstance(classLoaderKey));
        for (Class<T> clazz : loader)
            instances.add(injector.getInstance(clazz));
        return instances.toArray((T[]) Array.newInstance(type, instances.size()));
    }

    public static <T> ServiceLoaderProvider<T> of(Class<T> type) {
        return new ServiceLoaderProvider<T>(type);
    }
}
