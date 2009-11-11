package com.mycila.event.api;

import static com.mycila.event.api.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Subscriptions {

    private Subscriptions() {
    }

    public static <E, S> Subscription create(final TopicMatcher matcher, final Class<E> eventType, final S subscriber) {
        notNull(matcher, "TopicMatcher");
        notNull(eventType, "Event type");
        notNull(subscriber, "Subscriber");
        return new Subscription<E, S>() {
            final Reachability reachability = Reachability.of(subscriber);
            @Override
            public TopicMatcher topicMatcher() {
                return matcher;
            }

            @Override
            public Class<E> eventType() {
                return eventType;
            }

            @Override
            public S subscriber() {
                return subscriber;
            }

            @Override
            public Reachability reachability() {
                return reachability;
            }
        };
    }

}
