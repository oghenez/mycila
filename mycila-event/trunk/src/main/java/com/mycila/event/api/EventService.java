package com.mycila.event.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface EventService {
    <T> void subscribe(String topic, Class<T> eventType, Subscriber<T> subscriber);

    <T> void unsubscribe(String topic, Class<T> eventType, Subscriber<T> subscriber);

    <T> void publish(String topic, T event);

    <T> void register(String topic, Class<T> eventType, Vetoer<T> vetoer);
}
