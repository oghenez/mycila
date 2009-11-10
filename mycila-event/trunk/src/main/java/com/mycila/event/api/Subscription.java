package com.mycila.event.api;

import com.mycila.event.api.ref.Referencable;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Subscription<E, S> extends Referencable {
    TopicMatcher topicMatcher();

    Class<E> eventType();

    S subscriber();
}
