package com.mycila.plugin.scope;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * Same as {@link com.mycila.plugin.scope.Singleton}, except that the result is put in a Weak reference
 * so that it can be garbadged collected in case the export is not needed anymore.
 * The method will be called to get another instance if the reference has been garbadged collected.
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class WeakSingleton<T> extends ProviderSkeleton<T> {
    private volatile Reference<T> instance = new WeakReference<T>(null);

    @Override
    public T get() {
        T t = instance.get();
        if (t == null) {
            synchronized (this) {
                t = instance.get();
                if (t == null) {
                    t = context.invoke();
                    instance = new WeakReference<T>(t);
                }
            }
        }
        return t;
    }
}
