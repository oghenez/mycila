package com.mycila.plugin.scope.defaults;

import com.mycila.plugin.scope.ScopeProviderSkeleton;

/**
 * Singleton scope: the method is called once and the result kept in memory.
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Singleton<T> extends ScopeProviderSkeleton<T> {
    private volatile T instance;

    @Override
    public T get() {
        if (instance == null) {
            synchronized (this) {
                if (instance == null) {
                    instance = context.getInvokable().invoke();
                }
            }
        }
        return instance;
    }
}
