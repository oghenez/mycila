package com.mycila.event.impl;

import com.mycila.event.api.topic.TopicMatcher;
import com.mycila.event.api.util.Listener;
import com.mycila.event.api.util.ref.Reachability;
import com.mycila.event.api.util.ref.Referencable;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Subscription<E, S extends Listener<E>> implements Referencable {
    final TopicMatcher matcher;
    final Class<E> eventType;
    final S subscriber;

    Subscription(Class<E> eventType, TopicMatcher matcher, S subscriber) {
        this.eventType = eventType;
        this.matcher = matcher;
        this.subscriber = subscriber;
    }

    @Override
    public Reachability reachability() {
        return subscriber.reachability();
    }
}
