package com.mycila.ujd.impl;

import com.mycila.ujd.api.JVM;
import com.mycila.ujd.api.LoadedClass;
import com.mycila.ujd.api.Loader;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class JVMImpl implements JVM {

    

    public Iterable<? extends Loader> getLoaders() {
        return null;
    }

    public Iterable<? extends LoadedClass> getLoadedClasses() {
        return null;
    }

    public Iterable<? extends Loader> getContainers() {
        return null;
    }

    public Iterable<? extends Loader> getContainedClasses() {
        return null;
    }
}
