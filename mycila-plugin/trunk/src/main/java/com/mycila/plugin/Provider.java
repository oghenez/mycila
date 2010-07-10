package com.mycila.plugin;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Provider<T> {
    T get();
}
