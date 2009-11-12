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
import com.mycila.event.Event;
import com.mycila.event.Events;
import com.mycila.event.Subscriber;
import com.mycila.event.Subscription;
import com.mycila.event.Subscriptions;
import com.mycila.event.Topic;
import com.mycila.event.TopicMatcher;
import com.mycila.event.VetoableEvent;
import com.mycila.event.Vetoer;
import com.mycila.event.ref.ReferencableCollection;
import com.mycila.event.util.Provider;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.Executor;

import static com.mycila.event.util.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DefaultDispatcher implements Dispatcher {

    private final Collection<Subscription> subscribers = new ReferencableCollection<Subscription>();
    private final Collection<Subscription> vetoers = new ReferencableCollection<Subscription>();

    private final Provider<ErrorHandler> exceptionHandlerProvider;
    private final Executor publishExecutor;
    private final Executor subscriberExecutor;

    //TODO: executor providers instead of executors
    public DefaultDispatcher(Provider<ErrorHandler> exceptionHandlerProvider, Executor publishExecutor, Executor subscriberExecutor) {
        this.exceptionHandlerProvider = notNull(exceptionHandlerProvider, "ErrorHandlerProvider");
        this.publishExecutor = notNull(publishExecutor, "Publishing executor");
        this.subscriberExecutor = notNull(subscriberExecutor, "Subscriber executor");
    }

    @Override
    public final <E> void publish(final Topic topic, final E source) {
        notNull(topic, "Topic");
        notNull(source, "Event source");
        publishExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final Event<E> event = Events.event(topic, source);
                if (!isVetoed(event)) {
                    final ErrorHandler handler = exceptionHandlerProvider.get();
                    final Iterator<Subscriber<E>> subscriberIterator = getSubscribers(event);
                    try {
                        handler.onPublishingStarting();
                        while (subscriberIterator.hasNext()) {
                            final Subscriber<E> subscriber = subscriberIterator.next();
                            subscriberExecutor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        subscriber.onEvent(event);
                                    } catch (Exception e) {
                                        handler.onError(event, e);
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
        Iterator<Vetoer<E>> vetoerIterator = getVetoers(event);
        while (vetoerIterator.hasNext()) {
            vetoerIterator.next().check(vetoableEvent);
            if (!vetoableEvent.isAllowed())
                return true;
        }
        return false;
    }

    protected final <E, S> Iterator<S> getSubscribers(Event<E> event) {
        notNull(event, "Event");
        return filterListeners(subscribers, event);
    }

    protected final <E, S> Iterator<S> getVetoers(Event<E> event) {
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

    private <E, S> Iterator<S> filterListeners(final Iterable<Subscription> iterable, final Event<E> event) {
        return new Iterator<S>() {
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
            public S next() {
                if (!hasNext)
                    throw new NoSuchElementException();
                return (S) next.subscriber();
            }

            @Override
            public void remove() {
                subscriptionIterator.remove();
            }
        };
    }
}