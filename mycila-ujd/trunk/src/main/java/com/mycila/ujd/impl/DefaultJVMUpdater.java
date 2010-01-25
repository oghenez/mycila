package com.mycila.ujd.impl;

import com.mycila.ujd.api.JVM;
import com.mycila.ujd.api.JVMUpdater;

import java.util.Arrays;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DefaultJVMUpdater implements JVMUpdater {

    private final JVMImpl jvm = new JVMImpl();

    public JVM get() {
        return jvm;
    }

    public JVMUpdater addClasses(Class<?>... classes) {
        return addClasses(Arrays.asList(classes));
    }

    public JVMUpdater addClasses(Iterable<Class<?>> classes) {
        for (Class<?> aClass : classes)
            if (!aClass.isArray() // ignore arrays
                    && aClass.getClassLoader() != null) // ignore classes loaded by bootstrap classloader
                jvm.classRegistry.add(aClass);
        return this;
    }
}
