package com.mycila.event.impl;

import com.mycila.event.publisher.Publisher;
import com.mycila.event.topic.Topic;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface DestinationPublisher<E, D extends Topic> extends Publisher<E> {
    D destination();
}