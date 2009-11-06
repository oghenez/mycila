package com.mycila.event.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface DestinationPublisher<E, D extends Destination> extends Publisher<E> {
    D destination();
}