package com.mycila.event.api.message;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface MessageListener<T> {
    void onResponse(T value);
    void onError(Throwable t);
}
