package com.mycila.event.impl;

import com.mycila.event.api.EventService;
import com.mycila.event.api.event.Event;
import com.mycila.event.api.event.Events;
import com.mycila.event.api.event.VetoableEvent;
import com.mycila.event.api.exception.ExceptionHandler;
import com.mycila.event.api.exception.ExceptionHandlerProvider;
import com.mycila.event.api.exception.ExceptionHandlers;
import com.mycila.event.api.subscriber.Subscriber;
import com.mycila.event.api.topic.Topic;
import com.mycila.event.api.topic.TopicMatcher;
import com.mycila.event.impl.IdentityRefIterable;
import com.mycila.event.api.veto.Vetoer;

import java.io.Serializable;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class DefaultEventService implements EventService, Serializable {

    private static final long serialVersionUID = 0;

    private final ExceptionHandlerProvider exceptionHandlerProvider;
    private final IdentityRefIterable<Subscriber<?>> subscribers = new IdentityRefIterable<Subscriber<?>>();

    DefaultEventService() {
        this(ExceptionHandlers.rethrowExceptionsWhenFinishedProvider());
    }

    DefaultEventService(ExceptionHandlerProvider ExceptionHandlerProvider) {
        this.exceptionHandlerProvider = ExceptionHandlerProvider;
    }

    @Override
    public <E> void publish(Topic topic, E source) {
        Event<E> event = Events.event(topic, source);
        if (!isVetoed(event)) {
            ExceptionHandler handler = exceptionHandlerProvider.get();
            handler.onPublishingStarting();
            try {
                for (Subscriber<E> subscriber : findSubscribers(event)) {
                    try {
                        subscriber.onEvent(event);
                    } catch (Exception e) {
                        handler.onException(event, e);
                    }
                }
            } finally {
                handler.onPublishingFinished();
            }
        }
    }

    private <E> boolean isVetoed(Event<E> event) {
        VetoableEvent<E> vetoableEvent = Events.vetoable(event);
        for (Vetoer<E> vetoer : findVetoers(event)) {
            vetoer.check(vetoableEvent);
            if (!vetoableEvent.isAllowed())
                return true;
        }
        return false;
    }

    @Override
    public <E> void register(TopicMatcher matcher, Class<E> eventType, Vetoer<E> vetoer) {
    }

    @Override
    public <E> void subscribe(TopicMatcher matcher, Class<E> eventType, Subscriber<E> subscriber) {
    }

    @Override
    public <E> void unsubscribe(Subscriber<E> subscriber) {
    }

    private <E> Iterable<Vetoer<E>> findVetoers(Event<E> event) {
        return null;
    }

    private <E> Iterable<Subscriber<E>> findSubscribers(Event<E> event) {
        return null;
    }

}
