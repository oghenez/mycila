package com.mycila.event.impl;

import static com.mycila.event.api.Ensure.*;
import com.mycila.event.api.ErrorHandler;
import com.mycila.event.api.ErrorHandlerProvider;
import com.mycila.event.api.Event;
import com.mycila.event.api.EventService;
import com.mycila.event.api.Events;
import com.mycila.event.api.Subscriber;
import com.mycila.event.api.Subscription;
import com.mycila.event.api.Subscriptions;
import com.mycila.event.api.Topic;
import com.mycila.event.api.TopicMatcher;
import com.mycila.event.api.VetoableEvent;
import com.mycila.event.api.Vetoer;
import com.mycila.event.api.ref.ReferencableCollection;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
//TODO: optimize
final class DefaultEventService implements EventService, Serializable {

    private static final long serialVersionUID = 0;

    private final ErrorHandlerProvider exceptionHandlerProvider;
    private final ReferencableCollection<Subscription> subscribers = new ReferencableCollection<Subscription>();
    private final ReferencableCollection<Subscription> vetoers = new ReferencableCollection<Subscription>();

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
        Iterable<Vetoer<E>> listeners = filterListeners(vetoers, event);
        for (Vetoer<E> vetoer : listeners) {
            vetoer.check(vetoableEvent);
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

    @SuppressWarnings({"unchecked"})
    private <E, S> Iterable<S> filterListeners(Iterable<Subscription> iterable, Event<E> event) {
        LinkedList<S> subscribers = new LinkedList<S>();
        for (Subscription subscription : iterable) {
            if (subscription.eventType().isAssignableFrom(event.source().getClass())
                    && subscription.topicMatcher().matches(event.topic())) {
                subscribers.add((S) subscription.subscriber());
            }
        }
        return subscribers;
    }

}
