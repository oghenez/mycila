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

package com.mycila.event;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Executor;

import static com.mycila.event.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class DefaultDispatcher implements Dispatcher {

    private final Collection<Subscription> subscribers = new ReferencableCollection<Subscription>();
    private final Collection<Subscription> vetoers = new ReferencableCollection<Subscription>();

    private final Provider<? extends ErrorHandler> exceptionHandlerProvider;
    private final Executor publishExecutor;
    private final Executor subscriberExecutor;

    DefaultDispatcher(Provider<? extends ErrorHandler> exceptionHandlerProvider,
                      Executor publishExecutor,
                      Executor subscriberExecutor) {
        this.exceptionHandlerProvider = notNull(exceptionHandlerProvider, "ErrorHandlerProvider");
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
                if (!isVetoed(event)) {
                    final ErrorHandler handler = exceptionHandlerProvider.get();
                    final Iterator<Subscription<E, Subscriber<E>>> subscriptionIterator = getSubscribers(event);
                    try {
                        handler.onPublishingStarting();
                        while (subscriptionIterator.hasNext()) {
                            final Subscription<E, Subscriber<E>> subscription = subscriptionIterator.next();
                            subscriberExecutor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        subscription.subscriber().onEvent(event);
                                    } catch (Exception e) {
                                        handler.onError(subscription, event, e);
                                    }
                                }
                            });
                        }
                    } finally {
                        handler.onPublishingFinished();
                    }
                }
            }
        });
    }

    @Override
    public final <E> void register(TopicMatcher matcher, Class<E> eventType, Vetoer<E> vetoer) {
        notNull(matcher, "TopicMatcher");
        notNull(eventType, "Event type");
        notNull(vetoer, "Vetoer");
        vetoers.add(Subscriptions.create(matcher, eventType, vetoer));
    }

    @Override
    public final <E> void unregister(Vetoer<E> vetoer) {
        notNull(vetoer, "Vetoer");
        removeSubscription(vetoers, vetoer);
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

    protected final <E> boolean isVetoed(Event<E> event) {
        notNull(event, "Event");
        VetoableEvent<E> vetoableEvent = Events.vetoable(event);
        Iterator<Subscription<E, Vetoer<E>>> subscriptionIterator = getVetoers(event);
        while (subscriptionIterator.hasNext()) {
            subscriptionIterator.next().subscriber().check(vetoableEvent);
            if (!vetoableEvent.isAllowed())
                return true;
        }
        return false;
    }

    protected final <E> Iterator<Subscription<E, Subscriber<E>>> getSubscribers(Event<E> event) {
        notNull(event, "Event");
        return filterListeners(subscribers, event);
    }

    protected final <E> Iterator<Subscription<E, Vetoer<E>>> getVetoers(Event<E> event) {
        notNull(event, "Event");
        return filterListeners(vetoers, event);
    }

    private <S> void removeSubscription(Iterable<Subscription> iterable, S listener) {
        for (Iterator<Subscription> it = iterable.iterator(); it.hasNext();) {
            Subscription s = it.next();
            if (s.subscriber().equals(listener))
                it.remove();
        }
    }

    private <E, S> Iterator<Subscription<E, S>> filterListeners(final Iterable<Subscription> iterable, final Event<E> event) {
        return new Iterator<Subscription<E, S>>() {
            final Class<?> eventType = event.source().getClass();
            final Topic topic = event.topic();
            final Iterator<Subscription> subscriptionIterator = iterable.iterator();
            private Subscription next;
            private boolean hasNext = true;

            @Override
            public boolean hasNext() {
                while (subscriptionIterator.hasNext()) {
                    next = subscriptionIterator.next();
                    if (next.eventType().isAssignableFrom(eventType)
                            && next.topicMatcher().matches(topic))
                        return hasNext = true;
                }
                return hasNext = false;
            }

            @SuppressWarnings({"unchecked"})
            @Override
            public Subscription<E, S> next() {
                if (!hasNext)
                    throw new NoSuchElementException();
                return next;
            }

            @Override
            public void remove() {
                subscriptionIterator.remove();
            }
        };
    }
}