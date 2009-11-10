package com.mycila.event.api;

import com.mycila.event.api.subscriber.Subscriber;
import com.mycila.event.api.topic.Topic;
import com.mycila.event.api.topic.TopicMatcher;
import com.mycila.event.api.veto.Vetoer;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface EventService {
    <E> void publish(Topic topic, E source);

    <E> void subscribe(TopicMatcher matcher, Class<E> eventType, Subscriber<E> subscriber);

    <E> void unsubscribe(Subscriber<E> subscriber);

    <E> void register(TopicMatcher matcher, Class<E> eventType, Vetoer<E> vetoer);

    <E> void unregister(Vetoer<E> vetoer);
}
