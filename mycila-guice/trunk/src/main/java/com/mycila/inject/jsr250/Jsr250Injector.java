package com.mycila.inject.jsr250;

import com.google.inject.Injector;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Jsr250Injector extends Injector {
    void destroy();
}
