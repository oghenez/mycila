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

package com.mycila.event.internal;

import com.mycila.event.Dispatcher;
import com.mycila.event.ErrorHandler;
import com.mycila.event.Event;
import com.mycila.event.Subscriber;
import com.mycila.event.Subscription;

import java.util.Iterator;
import java.util.concurrent.Executor;

import static com.mycila.event.internal.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class DefaultDispatcher implements Dispatcher {

    private final SubscriptionManager subscriptionManager = new SubscriptionManager();
    private final ErrorHandler errorHandler;
    private final Executor publishExecutor;
    private final Executor subscriberExecutor;

    public DefaultDispatcher(ErrorHandler errorHandler,
                             Executor publishExecutor,
                             Executor subscriberExecutor) {
        this.errorHandler = notNull(errorHandler, "ErrorHandler");
        this.publishExecutor = notNull(publishExecutor, "Publishing executor");
        this.subscriberExecutor = notNull(subscriberExecutor, "Subscriber executor");
    }

    public final <E> void publish(final Topic topic, final E source) {
        notNull(topic, "Topic");
        notNull(source, "Event source");
        publishExecutor.execute(new Runnable() {
            public void run() {
                final Event<E> event = Events.event(topic, source);
                final Iterator<Subscription<E>> subscriptionIterator = subscriptionManager.getSubscriptions(event);
                while (subscriptionIterator.hasNext()) {
                    final Subscription<E> subscription = subscriptionIterator.next();
                    subscriberExecutor.execute(new Runnable() {
                        public void run() {
                            try {
                                subscription.getSubscriber().onEvent(event);
                            } catch (Exception e) {
                                errorHandler.onError(subscription, event, e);
                            }
                        }
                    });
                }
            }
        });
    }

    public final <E> void subscribe(TopicMatcher matcher, Class<?> eventType, Subscriber<E> subscriber) {
        notNull(matcher, "TopicMatcher");
        notNull(eventType, "Event type");
        notNull(subscriber, "Subscriber");
        subscriptionManager.addSubscription(Subscriptions.create(matcher, eventType, subscriber));
    }

    public final <E> void unsubscribe(Subscriber<E> subscriber) {
        notNull(subscriber, "Subscriber");
        subscriptionManager.removeSubscriber(subscriber);
    }

    public void close() {
    }

}