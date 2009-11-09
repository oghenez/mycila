package com.mycila.event.api.publisher;

import com.mycila.event.api.util.TopicBased;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Publisher<E> extends TopicBased {
    void publish(E source);
}
