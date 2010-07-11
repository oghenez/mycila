package com.mycila.plugin.scope;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Error2<T> extends ScopeProviderSkeleton<T> {
    public Error2() {
        throw new IllegalArgumentException("yo");
    }

    @Override
    public T get() {
        return context.invoke();
    }
}