package com.mycila.ujd.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface JVM {
    Iterable<? extends Loader> getLoaders();
    Iterable<? extends LoadedClass> getLoadedClasses();
    Iterable<? extends Loader> getContainers();
    Iterable<? extends Loader> getContainedClasses();
}
