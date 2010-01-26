package com.mycila.ujd.api;

import java.net.URL;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface ContainedClass {
    Container getContainer();

    String getClassName();

    String getPath();

    URL getURL();
}