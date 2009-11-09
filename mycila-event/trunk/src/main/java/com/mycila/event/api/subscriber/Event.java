package com.mycila.event.api.subscriber;

import com.mycila.event.api.topic.Topic;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Event<E> {
    Topic topic();
    E source();
}
