package com.mycila.event.impl;

import com.mycila.event.api.ErrorHandler;
import com.mycila.event.api.ErrorHandlerProvider;
import com.mycila.event.api.Event;
import com.mycila.event.api.Events;
import com.mycila.event.api.Subscriber;
import com.mycila.event.api.Topic;

import java.util.Iterator;

import static com.mycila.event.api.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class SynchronousNonBlockingDispatcher extends DispatcherSkeleton {

    private final ErrorHandlerProvider exceptionHandlerProvider;

    SynchronousNonBlockingDispatcher(ErrorHandlerProvider ExceptionHandlerProvider) {
        this.exceptionHandlerProvider = ExceptionHandlerProvider;
    }

    @Override
    public <E> void publish(Topic topic, E source) {
        notNull(topic, "Topic");
        notNull(source, "Event source");
        Event<E> event = Events.event(topic, source);
        if (!isVetoed(event)) {
            ErrorHandler handler = exceptionHandlerProvider.get();
            Iterator<Subscriber<E>> subscriberIterator = getSubscribers(event);
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

}
