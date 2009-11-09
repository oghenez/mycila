package com.mycila.event.api.event;

import com.mycila.event.api.topic.Topic;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Event<E> {
    Topic topic();
    long timestamp();
    E source();
}
