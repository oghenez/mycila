package com.mycila.event.api;

import static com.mycila.event.api.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Publishers {

    private Publishers() {
    }

    public <E> Publisher<E> publisher(final Dispatcher dispatcher, final Topic topic) {
        notNull(topic, "Topic");
        notNull(dispatcher, "Dispatcher");
        return new Publisher<E>() {
            @Override
            public Topic topic() {
                return topic;
            }

            @Override
            public void publish(E source) {
                dispatcher.publish(topic, source);
            }

            @Override
            public String toString() {
                return "Publisher{topic=" + topic + "}";
            }
        };
    }

}
