package com.mycila.event.api.exception;

import com.mycila.event.api.event.Event;

import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface ExceptionHandler {
    void onPublishingStarting();

    void onPublishingFinished();

    void onException(Event<?> event, Exception exception);

    boolean hasFailed();

    List<Exception> exceptions();
}
