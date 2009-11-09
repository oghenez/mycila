package com.mycila.event.api.publisher;

import com.mycila.event.api.EventService;
import com.mycila.event.api.topic.Topic;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Publishers {

    private Publishers() {
    }

    public <E> Publisher<E> publisher(final EventService eventService, final Topic topic) {
        return new Publisher<E>() {
            @Override
            public Topic topic() {
                return topic;
            }

            @Override
            public void publish(E source) {
                eventService.publish(topic, source);
            }
        };
    }

}
