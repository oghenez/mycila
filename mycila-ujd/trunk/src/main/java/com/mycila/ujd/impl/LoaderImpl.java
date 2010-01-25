package com.mycila.ujd.impl;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.mycila.ujd.api.Container;
import com.mycila.ujd.api.LoadedClass;
import com.mycila.ujd.api.Loader;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class LoaderImpl implements Loader {

    private final ClassLoader classLoader;
    private final JVMImpl jvm;
    private final int hashCode;

    LoaderImpl(JVMImpl jvm, ClassLoader classLoader) {
        this.jvm = jvm;
        this.classLoader = classLoader;
        this.hashCode = 31 * classLoader.hashCode() + classLoader.getClass().hashCode();
    }

    public Iterable<? extends Loader> getChilds() {
        return Iterables.filter(jvm.getLoaders(), new Predicate<Loader>() {
            public boolean apply(Loader input) {
                return input.getParent() == LoaderImpl.this;
            }
        });
    }

    public String getName() {
        return classLoader.toString();
    }

    public Loader getParent() {
        return jvm.loaderRegistry.get(get().getParent());
    }

    public Iterable<? extends Container> getContainers() {
        return classLoader instanceof URLClassLoader ?
                Iterables.transform(Arrays.asList(((URLClassLoader) classLoader).getURLs()), new Function<URL, Container>() {
                    public Container apply(URL from) {
                        return jvm.containerRegistry.get(from);
                    }
                }) : Collections.<Container>emptyList();
    }

    public Iterable<? extends LoadedClass> getClasses() {
        return Iterables.filter(jvm.getLoadedClasses(), new Predicate<LoadedClass>() {
            public boolean apply(LoadedClass input) {
                return input.getLoader() == LoaderImpl.this;
            }
        });
    }

    public ClassLoader get() {
        return classLoader;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoaderImpl loader = (LoaderImpl) o;
        return classLoader.equals(loader.classLoader);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        return getName();
    }
}
