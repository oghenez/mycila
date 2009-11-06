package com.mycila.event.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface DestinationPublisherFactory<E, D extends Destination> {
    DestinationPublisher<E, D> create(D destination);
}
