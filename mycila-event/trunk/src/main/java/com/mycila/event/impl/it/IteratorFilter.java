package com.mycila.event.impl.it;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface IteratorFilter<T> {
    boolean accept(T t);
}
