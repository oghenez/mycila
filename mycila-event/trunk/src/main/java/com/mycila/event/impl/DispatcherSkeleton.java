package com.mycila.event.impl;

import com.mycila.event.api.Dispatcher;
import com.mycila.event.api.Event;
import com.mycila.event.api.Events;
import com.mycila.event.api.Subscriber;
import com.mycila.event.api.Subscription;
import com.mycila.event.api.Subscriptions;
import com.mycila.event.api.Topic;
import com.mycila.event.api.TopicMatcher;
import com.mycila.event.api.VetoableEvent;
import com.mycila.event.api.Vetoer;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static com.mycila.event.api.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
abstract class DispatcherSkeleton implements Dispatcher {

    private final Collection<Subscription> subscribers = new ReferencableCollection<Subscription>();
    private final Collection<Subscription> vetoers = new ReferencableCollection<Subscription>();

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

    final <E> boolean isVetoed(Event<E> event) {
        VetoableEvent<E> vetoableEvent = Events.vetoable(event);
        Iterator<Vetoer<E>> vetoerIterator = getVetoers(event);
        while (vetoerIterator.hasNext()) {
            vetoerIterator.next().check(vetoableEvent);
            if (!vetoableEvent.isAllowed())
                return true;
        }
        return false;
    }

    final <E, S> Iterator<S> getSubscribers(Event<E> event) {
        return filterListeners(subscribers, event);
    }

    final <E, S> Iterator<S> getVetoers(Event<E> event) {
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