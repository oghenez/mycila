package com.mycila.event.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Subscriber<E> {
    void onEvent(E event) throws Exception;
}
