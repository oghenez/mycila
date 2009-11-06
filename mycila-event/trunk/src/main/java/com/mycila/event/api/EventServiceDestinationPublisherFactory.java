package com.mycila.event.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class EventServiceDestinationPublisherFactory<E, D extends Destination> implements DestinationPublisherFactory<E, D> {

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
