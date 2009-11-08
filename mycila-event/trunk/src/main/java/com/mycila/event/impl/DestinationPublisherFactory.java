package com.mycila.event.impl;

import com.mycila.event.topic.Topic;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface DestinationPublisherFactory<E, D extends Topic> {
    DestinationPublisher<E, D> create(D destination);
}
