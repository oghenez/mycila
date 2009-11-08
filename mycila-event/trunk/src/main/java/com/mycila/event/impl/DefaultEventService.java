package com.mycila.event.impl;

import com.mycila.event.EventService;
import com.mycila.event.subscriber.Subscriber;
import com.mycila.event.topic.TopicMatcher;
import com.mycila.event.veto.Vetoer;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DefaultEventService implements EventService {
    @Override
    public <E> void subscribe(TopicMatcher destination, Class<E> eventType, Subscriber<E> subscriber) {
    }

    @Override
    public <E> void register(TopicMatcher destination, Class<E> eventType, Vetoer<E> vetoer) {
    }

    @Override
    public <E> void unsubscribe(Subscriber<E> subscriber) {
    }

    @Override
    public <E> void publish(com.mycila.event.topic.Topic destination, E event) {
    }
}
