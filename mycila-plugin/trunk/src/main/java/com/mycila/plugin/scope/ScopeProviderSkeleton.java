package com.mycila.plugin.scope;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class ScopeProviderSkeleton<T> implements ScopeProvider<T> {

    protected volatile ScopeContext<T> context;

    @Override
    public void init(ScopeContext<T> context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "Scope." + getClass().getSimpleName() + "(" + context + ")";
    }
}
