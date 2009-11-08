package com.mycila.event;

import com.mycila.event.subscriber.Subscriber;
import com.mycila.event.topic.Topic;
import com.mycila.event.topic.TopicMatcher;
import com.mycila.event.veto.Vetoer;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface EventService {
    <E> void publish(Topic destination, E event);

    <E> void unsubscribe(Subscriber<E> subscriber);

    <E> void subscribe(TopicMatcher destination, Class<E> eventType, Subscriber<E> subscriber);

    <E> void register(TopicMatcher destination, Class<E> eventType, Vetoer<E> vetoer);
}
