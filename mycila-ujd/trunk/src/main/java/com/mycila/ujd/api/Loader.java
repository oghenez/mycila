package com.mycila.ujd.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Loader {
    String getName();

    Loader getParent();

    Iterable<? extends Loader> getChilds();

    Iterable<? extends Container> getContainers();

    Iterable<? extends JavaClass<?>> getClasses();

    ClassLoader get();
}
