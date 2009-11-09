package com.mycila.event.api.exception;

import com.mycila.event.api.event.Event;

import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface ExceptionHandler {
    void onStart();
    void onException(Event<?> event, Exception exception);
    void onEnd();
    boolean hasFailed();
    List<Exception> exceptions();
}
