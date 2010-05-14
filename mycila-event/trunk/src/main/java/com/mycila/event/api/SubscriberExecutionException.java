package com.mycila.event.api;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class SubscriberExecutionException extends DispatcherException {
    private SubscriberExecutionException(Throwable cause) {
        super(cause);
    }

    public static SubscriberExecutionException wrap(Throwable throwable) {
        while (throwable instanceof InvocationTargetException
                || throwable instanceof DispatcherException)
            throwable = throwable instanceof InvocationTargetException ?
                    ((InvocationTargetException) throwable).getTargetException() :
                    throwable.getCause();
        return new SubscriberExecutionException(throwable);
    }
}
