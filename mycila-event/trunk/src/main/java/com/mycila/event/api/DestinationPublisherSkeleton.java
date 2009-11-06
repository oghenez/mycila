package com.mycila.event.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class DestinationPublisherSkeleton<E, D extends Destination> implements DestinationPublisher<E, D> {

    private final EventService eventService;

    protected DestinationPublisherSkeleton(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public void publish(E event) {
        eventService.publish(destination(), event);
    }

    @Override
    public abstract D destination();

}
