package com.mycila.plugin.scope;

import java.lang.reflect.AnnotatedElement;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface ScopeResolver {
    ScopeBinding getScopeBinding(AnnotatedElement member);
}
