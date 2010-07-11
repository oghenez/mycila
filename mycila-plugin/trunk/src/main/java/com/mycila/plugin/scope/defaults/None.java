package com.mycila.plugin.scope.defaults;

import com.mycila.plugin.scope.ScopeProviderSkeleton;

/**
 * No scope, this is the default scope. Each time teh export is needed, the method will be called.
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class None<T> extends ScopeProviderSkeleton<T> {
    @Override
    public T get() {
        return context.getInvokable().invoke();
    }
}