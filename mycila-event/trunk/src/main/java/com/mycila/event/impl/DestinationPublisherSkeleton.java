package com.mycila.event.impl;

import com.mycila.event.EventService;
import com.mycila.event.topic.Topic;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class DestinationPublisherSkeleton<E, D extends Topic> implements DestinationPublisher<E, D> {

    private final EventService eventService;

    protected DestinationPublisherSkeleton(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public void publish(E event) {
        getEventService().publish(destination(), event);
    }

    protected EventService getEventService() {
        return eventService;
    }

    @Override
    public abstract D destination();

}
