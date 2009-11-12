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

package com.mycila.event.dispatch;

import com.mycila.event.ErrorHandler;
import com.mycila.event.ErrorHandlerProvider;
import com.mycila.event.Event;
import com.mycila.event.Events;
import com.mycila.event.Subscriber;
import com.mycila.event.Topic;

import java.util.Iterator;
import java.util.concurrent.Executor;

import static com.mycila.event.util.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class OrderedDispatcher extends DispatcherSkeleton {

    private final ErrorHandlerProvider exceptionHandlerProvider;
    private final Executor executor;

    public OrderedDispatcher(ErrorHandlerProvider exceptionHandlerProvider, Executor executor) {
        this.exceptionHandlerProvider = notNull(exceptionHandlerProvider, "ErrorHandlerProvider");
        this.executor = notNull(executor, "Executor");
    }

    @Override
    public final <E> void publish(final Topic topic, final E source) {
        notNull(topic, "Topic");
        notNull(source, "Event source");
        executor.execute(new Runnable() {
            @Override
            public void run() {
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
        });

    }

}
