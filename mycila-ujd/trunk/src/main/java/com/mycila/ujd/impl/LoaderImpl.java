package com.mycila.ujd.impl;

import com.mycila.ujd.api.Container;
import com.mycila.ujd.api.LoadedClass;
import com.mycila.ujd.api.Loader;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class LoaderImpl implements Loader {
    LoaderImpl(ClassLoader classLoader) {
    }

    public Iterable<? extends Loader> getChilds() {
        return null;
    }

    public String getName() {
        return null;
    }

    public Loader getParent() {
        return null;
    }

    public Iterable<? extends Container> getContainers() {
        return null;
    }

    public Iterable<? extends LoadedClass> getClasses() {
        return null;
    }
}
