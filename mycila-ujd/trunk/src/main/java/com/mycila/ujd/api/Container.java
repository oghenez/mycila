package com.mycila.ujd.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Container {
    Location getLocation();
    Iterable<? extends ContainedClass> getClasses();
}
