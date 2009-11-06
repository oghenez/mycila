package com.mycila.event.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Subscriber<T> {
    void onEvent(T event) throws Exception;
}
