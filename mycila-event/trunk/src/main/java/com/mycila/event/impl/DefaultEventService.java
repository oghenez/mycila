package com.mycila.event.impl;

import com.mycila.event.api.EventService;
import com.mycila.event.api.error.ErrorHandler;
import com.mycila.event.api.error.ErrorHandlerProvider;
import com.mycila.event.api.event.Event;
import com.mycila.event.api.event.Events;
import com.mycila.event.api.event.VetoableEvent;
import com.mycila.event.api.subscriber.Subscriber;
import com.mycila.event.api.topic.Topic;
import com.mycila.event.api.topic.TopicMatcher;
import com.mycila.event.api.util.Listener;
import com.mycila.event.api.veto.Vetoer;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class DefaultEventService implements EventService, Serializable {

    private static final long serialVersionUID = 0;

    private final ErrorHandlerProvider exceptionHandlerProvider;
    private final IdentityRefIterable<Subscription> subscribers = new IdentityRefIterable<Subscription>();
    private final IdentityRefIterable<Subscription> vetoers = new IdentityRefIterable<Subscription>();

    DefaultEventService(ErrorHandlerProvider ExceptionHandlerProvider) {
        this.exceptionHandlerProvider = ExceptionHandlerProvider;
    }

    @Override
    public <E> void publish(Topic topic, E source) {
        Event<E> event = Events.event(topic, source);
        if (!isVetoed(event)) {
            ErrorHandler handler = exceptionHandlerProvider.get();
            Iterable<Subscriber<E>> listeners = filterListeners(subscribers, event);
            try {
                handler.onPublishingStarting();
                for (Subscriber<E> subscriber : listeners) {
                    try {
                        subscriber.onEvent(event);
                    } catch (Exception e) {
                        handler.onError(event, e);
                    }
                }
            } finally {
                handler.onPublishingFinished();
            }
        }
    }

    private <E> boolean isVetoed(Event<E> event) {
        VetoableEvent<E> vetoableEvent = Events.vetoable(event);
        Iterable<Vetoer<E>> listeners = filterListeners(vetoers, event);
        for (Vetoer<E> vetoer : listeners) {
            vetoer.check(vetoableEvent);
            if (!vetoableEvent.isAllowed())
                return true;
        }
        return false;
    }

    @Override
    public <E> void register(TopicMatcher matcher, Class<E> eventType, Vetoer<E> vetoer) {
        vetoers.add(new Subscription(eventType, matcher, vetoer));
    }

    @Override
    public <E> void unregister(Vetoer<E> vetoer) {
        removeSubscription(vetoers, vetoer);
    }

    @Override
    public <E> void subscribe(TopicMatcher matcher, Class<E> eventType, Subscriber<E> subscriber) {
        subscribers.add(new Subscription(eventType, matcher, subscriber));
    }

    @Override
    public <E> void unsubscribe(Subscriber<E> subscriber) {
        removeSubscription(subscribers, subscriber);
    }

    private <E> void removeSubscription(Iterable<Subscription> iterable, Listener<E> listener) {
        for (Iterator<Subscription> it = iterable.iterator(); it.hasNext();) {
            Subscription s = it.next();
            if (s.subscriber.equals(listener))
                it.remove();
        }
    }

    @SuppressWarnings({"unchecked"})
    private <E, L extends Listener<E>> Iterable<L> filterListeners(Iterable<Subscription> iterable, Event<E> event) {
        LinkedList<L> subscribers = new LinkedList<L>();
        for (Subscription subscription : iterable) {
            if (subscription.eventType.isAssignableFrom(event.source().getClass())
                    && subscription.matcher.matches(event.topic())) {
                subscribers.add((L) subscription.subscriber);
            }
        }
        return subscribers;
    }

}
