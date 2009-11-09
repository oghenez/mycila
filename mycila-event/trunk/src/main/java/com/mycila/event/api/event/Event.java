package com.mycila.event.api.event;

import com.mycila.event.api.util.TopicBased;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Event<E> extends TopicBased {
    long timestamp();

    E source();
}
