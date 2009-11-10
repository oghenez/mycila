package com.mycila.event.api;

import com.mycila.event.api.EventServiceException;
import com.mycila.event.api.Event;
import com.mycila.event.api.Events;
import com.mycila.event.api.ErrorHandler;
import com.mycila.event.api.ErrorHandlers;
import com.mycila.event.api.Topics;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class ExceptionHandlerTest {

    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    @Test(expected = EventServiceException.class)
    public void test_toString() {
        Event<String> event = Events.event(Topics.topic("a"), "Hello !");
        ErrorHandler handler = ErrorHandlers.rethrowErrorsWhenFinished().get();
        handler.onPublishingStarting();
        handler.onError(event, new IllegalArgumentException("An IllegalArgumentException occured"));
        handler.onError(event, new InvocationTargetException(new NullPointerException("null")));
        handler.onError(event, new IllegalStateException(new NullPointerException("null")));
        handler.onPublishingFinished();
    }

    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    @Test(expected = IllegalArgumentException.class)
    public void test_rethrow_now() {
        Event<String> event = Events.event(Topics.topic("a"), "Hello !");
        ErrorHandler handler = ErrorHandlers.rethrowErrorsImmediately().get();
        handler.onPublishingStarting();
        handler.onError(event, new IllegalArgumentException("An IllegalArgumentException occured"));
        handler.onError(event, new InvocationTargetException(new NullPointerException("null")));
        handler.onPublishingFinished();
    }

    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    @Test(expected = EventServiceException.class)
    public void test_rethrow_now_caught_exc() {
        Event<String> event = Events.event(Topics.topic("a"), "Hello !");
        ErrorHandler handler = ErrorHandlers.rethrowErrorsImmediately().get();
        handler.onPublishingStarting();
        handler.onError(event, new InvocationTargetException(new NullPointerException("null"), "ah ah"));
        handler.onError(event, new IllegalArgumentException("An IllegalArgumentException occured"));
        handler.onPublishingFinished();
    }

    public static void main(String[] args) {
        new ExceptionHandlerTest().test_toString();
    }
}
