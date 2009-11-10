package com.mycila.event.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Event<E> extends TopicBased {
    long timestamp();

    E source();
}
