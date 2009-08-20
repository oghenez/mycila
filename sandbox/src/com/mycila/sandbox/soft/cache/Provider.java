package com.mycila.sandbox.soft.cache;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Provider<K, T> {
    T fetch(K key);
}
