package com.mycila.event.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Subscription<E, S> extends Referencable {
    TopicMatcher topicMatcher();

    Class<E> eventType();

    S subscriber();
}
