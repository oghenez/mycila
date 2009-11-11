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

package com.mycila.event.impl;

import com.mycila.event.api.Dispatcher;
import com.mycila.event.api.ErrorHandlerProvider;
import com.mycila.event.api.ErrorHandlers;
import com.mycila.event.api.Topic;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.mycila.event.api.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Dispatchers {

    private Dispatchers() {
    }

    public static Dispatcher synchronous(boolean blocking) {
        return synchronous(blocking, ErrorHandlers.rethrowErrorsWhenFinished());
    }

    public static Dispatcher synchronous(boolean blocking, ErrorHandlerProvider errorHandlerProvider) {
        final SynchronousNonBlockingDispatcher dispatcher = new SynchronousNonBlockingDispatcher(notNull(errorHandlerProvider, "ErrorHandlerProvider"));
        return !blocking ? dispatcher : new DispatcherDelegate(dispatcher) {
            final Lock publishing = new ReentrantLock();

            @Override
            public <E> void publish(Topic topic, E source) {
                publishing.lock();
                try {
                    delegate.publish(topic, source);
                } finally {
                    publishing.unlock();
                }
            }
        };
    }

}
