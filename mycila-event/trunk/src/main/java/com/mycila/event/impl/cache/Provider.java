package com.mycila.event.impl.cache;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Provider<K, V> {
    V fetch(K key);
}
