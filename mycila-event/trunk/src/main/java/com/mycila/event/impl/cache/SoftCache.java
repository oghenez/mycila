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
