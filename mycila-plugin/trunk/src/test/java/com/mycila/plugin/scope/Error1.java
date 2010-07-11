package com.mycila.plugin.scope;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Error1<T> extends ScopeProviderSkeleton<T> {
    private Error1(){}
    @Override
    public T get() {
        return context.getInvokable().invoke();
    }
}