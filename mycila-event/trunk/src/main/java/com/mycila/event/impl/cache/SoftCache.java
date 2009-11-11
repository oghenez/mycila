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

package com.mycila.event.impl.cache;

import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class SoftCache<K, V> implements Cache<K, V> {

    private static final Object NULL_VALUE = new Object();

    private final Provider<K, V> provider;
    private final Map<K, V> cache = new SoftHashMap<K, V>();
    private final boolean allowNULL;

    public SoftCache(Provider<K, V> provider) {
        this(provider, true);
    }

    public SoftCache(Provider<K, V> provider, boolean allowNULL) {
        this.provider = provider;
        this.allowNULL = allowNULL;
    }

    @Override
    public V get(K key) {
        synchronized (cache) {
            V val = cache.get(key);
            if (val == NULL_VALUE) return null;
            if (val != null) return val;
            return fetched(key);
        }
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void clear() {
        synchronized (cache) {
            cache.clear();
        }
    }

    @SuppressWarnings({"unchecked"})
    private V fetched(K key) {
        V value = provider.fetch(key);
        if (value == null && allowNULL) cache.put(key, (V) NULL_VALUE);
        else if (value != null) cache.put(key, value);
        return value;
    }

}
