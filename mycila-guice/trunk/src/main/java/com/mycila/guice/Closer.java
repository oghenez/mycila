package com.mycila.guice;

import com.google.inject.Scope;

import java.lang.annotation.Annotation;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Closer {
    void register(Class<? extends Annotation> annotationClass);

    void register(Scope scope);

    void close();
}
