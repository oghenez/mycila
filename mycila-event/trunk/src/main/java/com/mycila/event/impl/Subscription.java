package com.mycila.event.impl;

import com.mycila.event.api.topic.TopicMatcher;
import com.mycila.event.api.util.Listener;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
interface Subscription<E, S extends Listener<E>> {
    TopicMatcher matcher();

    Class<E> eventType();

    S subscriber();
}
