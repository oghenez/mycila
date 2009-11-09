package com.mycila.event.impl;

import com.mycila.event.EventService;
import com.mycila.event.api.subscriber.Subscriber;
import com.mycila.event.api.topic.Topic;
import com.mycila.event.api.topic.TopicMatcher;
import com.mycila.event.api.veto.Vetoer;

import java.io.Serializable;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class DefaultEventService implements EventService, Serializable {
    private static final long serialVersionUID = 0;
    private final ConcurrentLinkedQueue<Subscriber<?>> subscribers = new ConcurrentLinkedQueue<Subscriber<?>>();
    
    @Override
    public <E> void publish(Topic topic, E event) {
        
    }

    @Override
    public <E> void register(TopicMatcher matcher, Class<E> eventType, Vetoer<E> vetoer) {
    }

    @Override
    public <E> void subscribe(TopicMatcher matcher, Class<E> eventType, Subscriber<E> subscriber) {
    }

    @Override
    public <E> void unsubscribe(Subscriber<E> subscriber) {
    }
}
