package com.mycila.event.api;

import com.mycila.event.api.TopicBased;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Publisher<E> extends TopicBased {
    void publish(E source);
}
