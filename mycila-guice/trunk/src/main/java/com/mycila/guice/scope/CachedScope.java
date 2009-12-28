package com.mycila.guice.scope;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class CachedScope implements Scope {

    private final Map<Key<?>, CachedValue<?>> cache = new HashMap<Key<?>, CachedValue<?>>();
    private final long duration;

    CachedScope(long duration, TimeUnit unit) {
        this.duration = unit.toNanos(duration);
    }

    @Override
    public <T> Provider<T> scope(final Key<T> key, final Provider<T> creator) {
        return new Provider<T>() {
            @SuppressWarnings({"unchecked"})
            @Override
            public T get() {
                CachedValue<T> cachedValue = (CachedValue<T>) cache.get(key);
                if (cachedValue == null || cachedValue.hasExpired()) {
                    synchronized (cache) {
                        cachedValue = (CachedValue<T>) cache.get(key);
                        if (cachedValue == null || cachedValue.hasExpired())
                            cache.put(key, cachedValue = new CachedValue<T>(creator.get()));
                    }
                }
                return cachedValue.value;
            }
        };
    }

    private class CachedValue<T> {
        final T value;
        final long time;

        CachedValue(T value) {
            this.value = value;
            this.time = System.nanoTime();
        }

        boolean hasExpired() {
            return System.nanoTime() - time > duration;
        }
    }
}