package com.mycila.event.api.event;

import com.mycila.event.api.topic.Topic;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Events {

    private Events() {
    }

    public static <E> Event<E> event(final Topic topic, final E source) {
        return new Event<E>() {
            private final long timestamp = System.nanoTime();

            @Override
            public Topic topic() {
                return topic;
            }

            @Override
            public E source() {
                return source;
            }

            @Override
            public long timestamp() {
                return timestamp;
            }
        };
    }

    public static <E> VetoableEvent<E> vetoable(final Topic topic, final E source) {
        return vetoable(event(topic, source));
    }

    public static <E> VetoableEvent<E> vetoable(final Event<E> event) {
        return new VetoableEvent<E>() {
            private final AtomicBoolean allowed = new AtomicBoolean(true);

            @Override
            public Event<E> event() {
                return event;
            }

            @Override
            public void veto() {
                allowed.set(false);
            }

            @Override
            public boolean isAllowed() {
                return allowed.get();
            }
        };
    }

}
