package com.mycila.event.impl.cache;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Cache<K, V> {
    V get(K key);
    void clear();
    int size();
}
