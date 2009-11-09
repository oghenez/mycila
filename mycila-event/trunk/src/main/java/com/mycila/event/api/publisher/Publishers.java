package com.mycila.event.api.publisher;

import com.mycila.event.EventService;
import com.mycila.event.api.topic.Topic;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Publishers {

    private Publishers() {}

    public <E> Publisher<E> on(final EventService eventService, final Topic topic) {
        return new Publisher<E>() {
            @Override
            public Topic topic() {
                return topic;
            }

            @Override
            public void publish(E event) {
                eventService.publish(topic, event);
            }
        };
    }

}
