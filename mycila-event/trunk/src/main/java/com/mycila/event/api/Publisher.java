package com.mycila.event.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Publisher<E> extends TopicBased {
    void publish(E source);
}
