package com.mycila.guice;

import com.google.inject.Scope;

import java.lang.annotation.Annotation;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface ScopeWithAnnotation extends Scope {
    Class<? extends Annotation> getScopeAnnotation();
}
