package com.mycila.ujd.api;

import com.google.common.base.Predicate;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface JVM {
    Iterable<? extends Loader> getLoaders();

    Iterable<? extends JavaClass<?>> getClasses();

    <T extends JavaClass<?>> Iterable<? extends T> getClasses(Predicate<? super T> predicate);

    Iterable<? extends Container> getContainers();

    Iterable<? extends ContainedClass> getContainedClasses();
}
