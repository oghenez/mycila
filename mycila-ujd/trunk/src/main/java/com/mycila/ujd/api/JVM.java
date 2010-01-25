package com.mycila.ujd.api;

import com.google.common.base.Predicate;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface JVM {
    <T extends JavaClass<?>> Iterable<T> getClasses(Predicate<? super JavaClass<?>> predicate);

    Iterable<? extends Loader> getLoaders();
    Iterable<? extends LoadedClass> getLoadedClasses();
    Iterable<? extends Container> getContainers();
    Iterable<? extends ContainedClass> getContainedClasses();
}
