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
import com.mycila.event.api.veto.Vetoer;

import java.io.Serializable;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class DefaultEventService implements EventService, Serializable {

    private static final long serialVersionUID = 0;

    private final ExceptionHandlerProvider ExceptionHandlerProvider;

    //private final ConcurrentLinkedQueue<Subscriber<?>> subscribers = new ConcurrentLinkedQueue<Subscriber<?>>();

    DefaultEventService() {
        this(ExceptionHandlers.cumulativeProvider());
    }

    DefaultEventService(ExceptionHandlerProvider ExceptionHandlerProvider) {
        this.ExceptionHandlerProvider = ExceptionHandlerProvider;
    }

    @Override
    public <E> void publish(Topic topic, E source) {
        Event<E> event = Events.event(topic, source);
        if (!isVetoed(event)) {
            List<Subscriber<E>> subscribers = findSubscribers(event);
            ExceptionHandler handler = ExceptionHandlerProvider.get();
            handler.onStart();
            for (Subscriber<E> subscriber : subscribers) {
                try {
                    subscriber.onEvent(event);
                } catch (Exception e) {
                    handler.onException(event, e);
                }
            }
            handler.onEnd();
        }
    }

    private <E> boolean isVetoed(Event<E> event) {
        List<Vetoer<E>> vetoers = findVetoers(event);
        if (!vetoers.isEmpty()) {
            VetoableEvent<E> vetoableEvent = Events.vetoable(event);
            for (Vetoer<E> vetoer : vetoers) {

            }
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

    private <E> List<Vetoer<E>> findVetoers(Event<E> event) {
        return null;
    }

    private <E> List<Subscriber<E>> findSubscribers(Event<E> event) {
        return null;
    }

}
