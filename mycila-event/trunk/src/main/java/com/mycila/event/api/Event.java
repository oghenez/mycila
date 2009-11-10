package com.mycila.event.api;

import com.mycila.event.api.TopicBased;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Event<E> extends TopicBased {
    long timestamp();

    E source();
}
