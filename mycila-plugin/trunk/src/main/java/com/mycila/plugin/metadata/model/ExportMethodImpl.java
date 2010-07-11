package com.mycila.plugin.metadata.model;

import com.mycila.plugin.metadata.ExportMethod;

import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ExportMethodImpl<T> implements ExportMethod<T> {

    public ExportMethodImpl(Method method, Class<T> type) {

    }

    @Override
    public T invoke() {
        return null;
    }

    @Override
    public ExportScopeImpl getScope() {
        return null;
    }
}
