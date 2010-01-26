package com.mycila.ujd.api;

import java.net.URL;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Container {
    URL getURL();

    Iterable<? extends ContainedClass> getClasses();
}
