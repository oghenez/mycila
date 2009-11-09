package com.mycila.event.api;

import com.mycila.event.api.EventServiceException;
import com.mycila.event.api.event.Event;
import com.mycila.event.api.event.Events;
import com.mycila.event.api.exception.ExceptionHandler;
import com.mycila.event.api.exception.ExceptionHandlers;
import com.mycila.event.api.topic.Topics;
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
        ExceptionHandler handler = ExceptionHandlers.rethrowExceptionsWhenFinished();
        handler.onPublishingStarting();
        handler.onException(event, new IllegalArgumentException("An IllegalArgumentException occured"));
        handler.onException(event, new InvocationTargetException(new NullPointerException("null")));
        handler.onException(event, new IllegalStateException(new NullPointerException("null")));
        handler.onPublishingFinished();
    }

    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    @Test(expected = IllegalArgumentException.class)
    public void test_rethrow_now() {
        Event<String> event = Events.event(Topics.topic("a"), "Hello !");
        ExceptionHandler handler = ExceptionHandlers.rethrowExceptionsImmediately();
        handler.onPublishingStarting();
        handler.onException(event, new IllegalArgumentException("An IllegalArgumentException occured"));
        handler.onException(event, new InvocationTargetException(new NullPointerException("null")));
        handler.onPublishingFinished();
    }

    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    @Test(expected = EventServiceException.class)
    public void test_rethrow_now_caught_exc() {
        Event<String> event = Events.event(Topics.topic("a"), "Hello !");
        ExceptionHandler handler = ExceptionHandlers.rethrowExceptionsImmediately();
        handler.onPublishingStarting();
        handler.onException(event, new InvocationTargetException(new NullPointerException("null"), "ah ah"));
        handler.onException(event, new IllegalArgumentException("An IllegalArgumentException occured"));
        handler.onPublishingFinished();
    }

    public static void main(String[] args) {
        new ExceptionHandlerTest().test_toString();
    }
}
