package com.mycila.event.impl;

import com.mycila.event.api.*;
import com.mycila.event.impl.cache.Provider;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import static com.mycila.event.api.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
//TODO: optimize
final class DefaultEventService implements EventService, Serializable {

    private static final long serialVersionUID = 0;

    private final ErrorHandlerProvider exceptionHandlerProvider;
    private final Collection<Subscription> subscribers = new ReferencableCollection<Subscription>();
    private final Collection<Subscription> vetoers = new ReferencableCollection<Subscription>();

    DefaultEventService(ErrorHandlerProvider ExceptionHandlerProvider) {
        this.exceptionHandlerProvider = ExceptionHandlerProvider;
    }

    @Override
    public <E> void publish(Topic topic, E source) {
        notNull(topic, "Topic");
        notNull(source, "Event source");
        Event<E> event = Events.event(topic, source);
        if (!isVetoed(event)) {
            ErrorHandler handler = exceptionHandlerProvider.get();
            Iterator<Subscriber<E>> subscriberIterator = getFilteredIterator(subscribers, event);
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

    @Override
    public <E> void register(TopicMatcher matcher, Class<E> eventType, Vetoer<E> vetoer) {
        notNull(matcher, "TopicMatcher");
        notNull(eventType, "Event type");
        notNull(vetoer, "Vetoer");
        vetoers.add(Subscriptions.create(matcher, eventType, vetoer));
    }

    @Override
    public <E> void unregister(Vetoer<E> vetoer) {
        notNull(vetoer, "Vetoer");
        removeSubscription(vetoers, vetoer);
    }

    @Override
    public <E> void subscribe(TopicMatcher matcher, Class<E> eventType, Subscriber<E> subscriber) {
        notNull(matcher, "TopicMatcher");
        notNull(eventType, "Event type");
        notNull(subscriber, "Subscriber");
        subscribers.add(Subscriptions.create(matcher, eventType, subscriber));
    }

    @Override
    public <E> void unsubscribe(Subscriber<E> subscriber) {
        notNull(subscriber, "Subscriber");
        removeSubscription(subscribers, subscriber);
    }

    private <E> boolean isVetoed(Event<E> event) {
        VetoableEvent<E> vetoableEvent = Events.vetoable(event);
        Iterator<Vetoer<E>> vetoerIterator = getFilteredIterator(vetoers, event);
        while (vetoerIterator.hasNext()) {
            vetoerIterator.next().check(vetoableEvent);
            if (!vetoableEvent.isAllowed())
                return true;
        }
        return false;
    }

    private <S> void removeSubscription(Iterable<Subscription> iterable, S listener) {
        for (Iterator<Subscription> it = iterable.iterator(); it.hasNext();) {
            Subscription s = it.next();
            if (s.subscriber().equals(listener))
                it.remove();
        }
    }

    //@SuppressWarnings({"unchecked"})
    private <E, S> Iterator<S> getFilteredIterator(Iterable<Subscription> iterable, Event<E> event) {
        Iterator<Subscription> subscriptionIterator = iterable.iterator();
        return new Iterator<S>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public S next() {
                return null;
            }

            @Override
            public void remove() {
            }
        };

    }

    LinkedList<S> subscribers = new LinkedList<S>();
        for (Subscription subscription : iterable) {
            if (subscription.eventType().isAssignableFrom(event.source().getClass())
                    && subscription.topicMatcher().matches(event.topic())) {
                subscribers.add((S) subscription.subscriber());
                return subscribers;
            }

            private static final class CacheProvider implements Provider<Topic, Iterable<Subscription>> {
                private final Iterable<Subscription> subscriptions;

                private CacheProvider(Iterable<Subscription> subscriptions) {
                    this.subscriptions = subscriptions;
                }

                @Override
                public Iterable<Subscription> fetch(Topic topic) {
                    return new Iterable<Subscription>() {
                        @Override
                        public Iterator<Subscription> iterator() {
                            Iterator<Subscription> it = subscriptions.iterator();
                            return new Iterator<Subscription>() {


                                @Override
                                public boolean hasNext() {
                                    return false;
                                }

                                @Override
                                public Subscription next() {
                                    return null;
                                }

                                @Override
                                public void remove() {
                                }
                            };
                        }
                    };

                    for (Subscription subscription : subscriptions)
                        if (subscription.topicMatcher().matches(topic))
                            subscribersForTopic.add(subscription);
                    return subscribersForTopic;
                }
            }
        }
