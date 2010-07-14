package com.mycila.plugin.scope;

import java.lang.annotation.Annotation;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface ScopeBinding {
    Scope getScope();
    Annotation getAnnotation();
}
