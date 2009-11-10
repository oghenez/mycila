package com.mycila.event.api.publisher;

import com.mycila.event.api.EventService;
import static com.mycila.event.api.util.Ensure.*;
import com.mycila.event.api.topic.Topic;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Publishers {

    private Publishers() {
    }

    public <E> Publisher<E> publisher(final EventService eventService, final Topic topic) {
        notNull(topic, "Topic");
        notNull(eventService, "EventService");
        return new Publisher<E>() {
            @Override
            public Topic topic() {
                return topic;
            }

            @Override
            public void publish(E source) {
                eventService.publish(topic, source);
            }

            @Override
            public String toString() {
                return "Publisher{topic=" + topic + "}";
            }
        };
    }

}
