package com.mycila.ujd.impl;

import com.mycila.ujd.api.JVM;
import com.mycila.ujd.api.JVMUpdater;

import java.util.Arrays;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DefaultJVMUpdater implements JVMUpdater {

    final JVMImpl jvm = new JVMImpl();

    public JVM get() {
        return jvm;
    }

    public JVMUpdater addClasses(Class<?>... classes) {
        return addClasses(Arrays.asList(classes));
    }

    public JVMUpdater addLoaders(ClassLoader... classLoaders) {
        return addLoaders(Arrays.asList(classLoaders));
    }

    public JVMUpdater addLoaders(Iterable<? extends ClassLoader> classLoaders) {
        return null;
    }

    public JVMUpdater addClasses(Iterable<Class<?>> classes) {
        return null;
    }
}
