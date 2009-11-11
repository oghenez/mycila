package com.mycila.event.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Dispatcher {
    <E> void publish(Topic topic, E source);

    <E> void subscribe(TopicMatcher matcher, Class<E> eventType, Subscriber<E> subscriber);

    <E> void unsubscribe(Subscriber<E> subscriber);

    <E> void register(TopicMatcher matcher, Class<E> eventType, Vetoer<E> vetoer);

    <E> void unregister(Vetoer<E> vetoer);
}
