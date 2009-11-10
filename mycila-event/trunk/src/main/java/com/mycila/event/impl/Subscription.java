package com.mycila.event.impl;

import com.mycila.event.api.TopicMatcher;
import com.mycila.event.api.util.Listener;
import com.mycila.event.api.Reachability;
import com.mycila.event.impl.Referencable;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Subscription implements Referencable {
    final TopicMatcher matcher;
    final Class<?> eventType;
    final Listener<?> subscriber;

    Subscription(Class<?> eventType, TopicMatcher matcher, Listener<?> subscriber) {
        this.eventType = eventType;
        this.matcher = matcher;
        this.subscriber = subscriber;
    }

    @Override
    public Reachability reachability() {
        return subscriber.reachability();
    }
}
