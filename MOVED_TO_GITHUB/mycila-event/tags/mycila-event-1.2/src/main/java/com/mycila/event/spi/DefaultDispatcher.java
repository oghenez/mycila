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

import com.mycila.event.api.Dispatcher;
import com.mycila.event.api.Event;
import com.mycila.event.api.FilterIterator;
import com.mycila.event.api.Subscriber;
import com.mycila.event.api.Subscription;
import com.mycila.event.api.Topic;
import com.mycila.event.api.TopicMatcher;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Executor;

import static com.mycila.event.api.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class DefaultDispatcher implements Dispatcher {

    private final Collection<Subscription> subscribers = new ReferencableCollection<Subscription>();

    private final ErrorHandler errorHandler;
    private final Executor publishExecutor;
    private final Executor subscriberExecutor;

    DefaultDispatcher(ErrorHandler errorHandler,
                      Executor publishExecutor,
                      Executor subscriberExecutor) {
        this.errorHandler = notNull(errorHandler, "ErrorHandler");
        this.publishExecutor = notNull(publishExecutor, "Publishing executor");
        this.subscriberExecutor = notNull(subscriberExecutor, "Subscriber executor");
    }

    @Override
    public final <E> void publish(final Topic topic, final E source) {
        notNull(topic, "Topic");
        notNull(source, "Event source");
        publishExecutor.execute(new Runnable() {
            @SuppressWarnings({"unchecked"})
            @Override
            public void run() {
                final Event<E> event = Events.event(topic, source);
                final Iterator<Subscription<E, Subscriber<E>>> subscriptionIterator = getSubscribers(event);
                try {
                    errorHandler.onPublishingStarting(event);
                    while (subscriptionIterator.hasNext()) {
                        final Subscription<E, Subscriber<E>> subscription = subscriptionIterator.next();
                        subscriberExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    subscription.getSubscriber().onEvent(event);
                                } catch (Exception e) {
                                    errorHandler.onError(subscription, event, e);
                                }
                            }
                        });
                    }
                } finally {
                    errorHandler.onPublishingFinished(event);
                }
            }
        });
    }

    @Override
    public final <E> void subscribe(TopicMatcher matcher, Class<E> eventType, Subscriber<E> subscriber) {
        notNull(matcher, "TopicMatcher");
        notNull(eventType, "Event type");
        notNull(subscriber, "Subscriber");
        subscribers.add(Subscriptions.create(matcher, eventType, subscriber));
    }

    @Override
    public final <E> void unsubscribe(Subscriber<E> subscriber) {
        notNull(subscriber, "Subscriber");
        removeSubscription(subscribers, subscriber);
    }

    @Override
    public void close() {
    }

    protected final <E> Iterator<Subscription<E, Subscriber<E>>> getSubscribers(Event<E> event) {
        notNull(event, "Event");
        return filterListeners(subscribers, event);
    }

    private <S> void removeSubscription(Iterable<Subscription> iterable, S listener) {
        for (Iterator<Subscription> it = iterable.iterator(); it.hasNext();) {
            Subscription s = it.next();
            if (s.getSubscriber().equals(listener))
                it.remove();
        }
    }

    private <E, S> Iterator<Subscription<E, S>> filterListeners(final Iterable<Subscription> iterable, final Event<E> event) {
        return new FilterIterator<Subscription<E, S>, Subscription>(iterable.iterator()) {
            final Class<?> eventType = event.getSource().getClass();
            final Topic topic = event.getTopic();

            @SuppressWarnings({"unchecked"})
            @Override
            protected Subscription<E, S> filter(Subscription delegate) {
                if (delegate.getEventType().isAssignableFrom(eventType) && delegate.getTopicMatcher().matches(topic))
                    return delegate;
                return null;
            }
        };
    }
}