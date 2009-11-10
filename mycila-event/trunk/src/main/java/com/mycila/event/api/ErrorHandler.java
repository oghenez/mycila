package com.mycila.event.api;

import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface ErrorHandler {
    void onPublishingStarting();

    void onPublishingFinished();

    void onError(Event<?> event, Exception e);

    boolean hasFailed();

    List<Exception> errors();
}
