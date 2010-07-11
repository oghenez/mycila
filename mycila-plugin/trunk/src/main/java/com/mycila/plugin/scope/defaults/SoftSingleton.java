package com.mycila.plugin.scope.defaults;

import com.mycila.plugin.scope.ScopeProviderSkeleton;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;

/**
 * Same as {@link Singleton}, except that the result is put in a Sof reference
 * so that it can be garbadged collected in case of low memory. The method will be called to get another instance
 * if the reference has been garbadged collected.
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class SoftSingleton<T> extends ScopeProviderSkeleton<T> {
    private volatile Reference<T> instance = new SoftReference<T>(null);

    @Override
    public T get() {
        T t = instance.get();
        if (t == null) {
            synchronized (this) {
                t = instance.get();
                if (t == null) {
                    t = context.getInvokable().invoke();
                    instance = new SoftReference<T>(t);
                }
            }
        }
        return t;
    }
}