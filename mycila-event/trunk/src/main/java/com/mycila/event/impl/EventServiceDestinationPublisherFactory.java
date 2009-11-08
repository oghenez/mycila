package com.mycila.event.impl;

import com.mycila.event.EventService;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class EventServiceDestinationPublisherFactory<E, D extends com.mycila.event.topic.Topic> implements DestinationPublisherFactory<E, D> {

    private final EventService eventService;

    public EventServiceDestinationPublisherFactory(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public DestinationPublisher<E, D> create(final D destination) {
        return new DestinationPublisherSkeleton<E, D>(eventService) {
            @Override
            public D destination() {
                return destination;
            }
        };
    }

}
