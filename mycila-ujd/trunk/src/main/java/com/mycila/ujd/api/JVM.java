package com.mycila.ujd.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface JVM {
    Iterable<? extends Loader> getLoaders();
    Iterable<? extends LoadedClass> getLoadedClasses();
    Iterable<? extends Container> getContainers();
    Iterable<? extends ContainedClass> getContainedClasses();
}
