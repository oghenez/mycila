package com.mycila.plugin.metadata;

import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ExportMetadata<T> {

    public ExportMetadata(Method method, Class<T> type) {
        
    }

    public T get() {
        return null;
    }

    public ScopeMetadata getScope() {
        return null;
    }
}
