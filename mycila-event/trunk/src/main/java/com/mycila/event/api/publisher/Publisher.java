package com.mycila.event.api.publisher;

import com.mycila.event.api.topic.Topic;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Publisher<E> {
    Topic topic();
    void publish(E event);
}
