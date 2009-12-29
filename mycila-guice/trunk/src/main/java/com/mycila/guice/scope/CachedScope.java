/**
 * Copyright (C) 2009 Mathieu Carbou <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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