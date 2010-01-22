package com.mycila.ujd.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface JVMUpdater {
    JVM get();
    JVMUpdater addLoaders(ClassLoader... classLoaders);
    JVMUpdater addLoaders(Iterable<? extends ClassLoader> classLoaders);
    JVMUpdater addClasses(Class<?>... classes);
    JVMUpdater addClasses(Iterable<Class<?>> classes);
}
