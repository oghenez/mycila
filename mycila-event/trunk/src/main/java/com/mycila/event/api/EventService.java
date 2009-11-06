package com.mycila.event.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface EventService {
    <E> void subscribe(DestinationMatcher destination, Class<E> eventType, Subscriber<E> subscriber);

    <E> void register(DestinationMatcher destination, Class<E> eventType, Vetoer<E> vetoer);

    <E> void unsubscribe(Subscriber<E> subscriber);

    <E> void publish(Destination destination, E event);
}
