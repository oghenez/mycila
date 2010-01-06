package com.mycila.event.api.message;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface MessageRequest<R> {
    R getResponse() throws InterruptedException;
    R getResponse(long timeout, TimeUnit unit) throws TimeoutException, InterruptedException;
}
