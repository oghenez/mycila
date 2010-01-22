package com.mycila.ujd.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface JVMUpdater {
    JVM get();
    JVMUpdater addClasses(Class<?>... classes);
    JVMUpdater addClasses(Iterable<Class<?>> classes);
}
