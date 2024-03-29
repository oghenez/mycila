/**
 * Copyright (C) 2009 Mathieu Carbou <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mycila.event.spi;

import com.mycila.event.api.DispatcherException;
import com.mycila.event.api.Event;
import com.mycila.event.api.Topics;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class ErrorHandlerTest {

    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    @Test(expected = DispatcherException.class)
    public void test_toString() {
        Event<String> event = Events.event(Topics.topic("a"), "Hello !");
        ErrorHandler handler = ErrorHandlers.rethrowErrorsAfterPublish();
        handler.onPublishingStarting(null);
        handler.onError(null, event, new IllegalArgumentException("An IllegalArgumentException occured"));
        handler.onError(null, event, new InvocationTargetException(new NullPointerException("null")));
        handler.onError(null, event, new IllegalStateException(new NullPointerException("null")));
        handler.onPublishingFinished(null);
    }

    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    @Test(expected = IllegalArgumentException.class)
    public void test_rethrow_now() {
        Event<String> event = Events.event(Topics.topic("a"), "Hello !");
        ErrorHandler handler = ErrorHandlers.rethrowErrorsImmediately();
        handler.onPublishingStarting(null);
        handler.onError(null, event, new IllegalArgumentException("An IllegalArgumentException occured"));
        handler.onError(null, event, new InvocationTargetException(new NullPointerException("null")));
        handler.onPublishingFinished(null);
    }

    @SuppressWarnings({"ThrowableInstanceNeverThrown"})
    @Test(expected = NullPointerException.class)
    public void test_rethrow_now_caught_exc() {
        Event<String> event = Events.event(Topics.topic("a"), "Hello !");
        ErrorHandler handler = ErrorHandlers.rethrowErrorsImmediately();
        handler.onPublishingStarting(null);
        handler.onError(null, event, new InvocationTargetException(new NullPointerException("null"), "ah ah"));
        handler.onError(null, event, new IllegalArgumentException("An IllegalArgumentException occured"));
        handler.onPublishingFinished(null);
    }

    public static void main(String[] args) {
        new ErrorHandlerTest().test_toString();
    }
}
