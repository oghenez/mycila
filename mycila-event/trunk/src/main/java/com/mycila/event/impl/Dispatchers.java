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
import com.mycila.event.api.Topic;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.mycila.event.api.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public enum Dispatchers {

    SYNCHRONOUS_BLOCKING_DISPATCHER {
        @Override
        Dispatcher createInternal(ErrorHandlerProvider errorHandlerProvider) {
            return new SynchronousUnorderedDispatcher(errorHandlerProvider) {
                final Lock publishing = new ReentrantLock();

                @Override
                public <E> void publish(Topic topic, E source) {
                    publishing.lock();
                    try {
                        super.publish(topic, source);
                    } finally {
                        publishing.unlock();
                    }
                }
            };
        }},

    SYNCHRONOUS_DISPATCHER {
        @Override
        Dispatcher createInternal(ErrorHandlerProvider errorHandlerProvider) {
            return new SynchronousUnorderedDispatcher(errorHandlerProvider);
        }},

    ASYNCHRONOUS_ORDERED_DISPATCHER {
        @Override
        Dispatcher createInternal(ErrorHandlerProvider errorHandlerProvider) {
            return null;
        }},

    ASYNCHRONOUS_UNORDERED_DISPATCHER {
        @Override
        Dispatcher createInternal(ErrorHandlerProvider errorHandlerProvider) {
            return null;
        }},

    BROADCAST_ORDERED_DISPATCHER {
        @Override
        Dispatcher createInternal(ErrorHandlerProvider errorHandlerProvider) {
            return null;
        }},

    BROADCAST_UNORDERED_DISPATCHER {
        @Override
        Dispatcher createInternal(ErrorHandlerProvider errorHandlerProvider) {
            return null;
        }};

    public Dispatcher create(ErrorHandlerProvider errorHandlerProvider) {
        return createInternal(notNull(errorHandlerProvider, "ErrorHandlerProvider"));
    }

    abstract Dispatcher createInternal(ErrorHandlerProvider errorHandlerProvider);

}
