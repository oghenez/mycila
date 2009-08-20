package com.mycila.sandbox.soft.cache;

import com.mycila.sandbox.soft.SoftHashMap;

import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class SoftCache<K, V> {

    public static final long INFINITE = -1;

    private final Provider<K, V> provider;
    private static final Object NULL_VALUE = new Object();
    private final Map<K, V> cache = new SoftHashMap<K, V>();

    public SoftCache(Provider<K, V> provider) {
        this.provider = provider;
    }

    public V get(K key) {
        V val = cache.get(key);
        if (val == NULL_VALUE) return null;
        if (val != null) return val;
        return fetched(key);
    }

    @SuppressWarnings({"unchecked"})
    private V fetched(K key) {
        V value = provider.fetch(key);
        cache.put(key, value == null ? (V) NULL_VALUE : value);
        return value;
    }

    public void clear() {
        synchronized (cache) {
            cache.clear();
        }
    }
}
