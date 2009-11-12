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

import com.mycila.event.api.ErrorHandler;
import com.mycila.event.api.ErrorHandlerProvider;
import com.mycila.event.api.Event;
import com.mycila.event.api.Events;
import com.mycila.event.api.Subscriber;
import com.mycila.event.api.Topic;

import java.util.Iterator;

import static com.mycila.event.api.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class SynchronousUnorderedDispatcher extends DispatcherSkeleton {

    private final ErrorHandlerProvider exceptionHandlerProvider;

    SynchronousUnorderedDispatcher(ErrorHandlerProvider ExceptionHandlerProvider) {
        this.exceptionHandlerProvider = ExceptionHandlerProvider;
    }

    @Override
    public <E> void publish(Topic topic, E source) {
        notNull(topic, "Topic");
        notNull(source, "Event source");
        Event<E> event = Events.event(topic, source);
        if (!isVetoed(event)) {
            ErrorHandler handler = exceptionHandlerProvider.get();
            Iterator<Subscriber<E>> subscriberIterator = getSubscribers(event);
            try {
                handler.onPublishingStarting();
                while (subscriberIterator.hasNext()) {
                    try {
                        subscriberIterator.next().onEvent(event);
                    } catch (Exception e) {
                        handler.onError(event, e);
                    }
                }
            } finally {
                handler.onPublishingFinished();
            }
        }
    }

}
