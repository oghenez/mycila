package com.mycila.math.concurrent;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Operation<T> {
    boolean isEmpty();
    int size();
    T take();
}
