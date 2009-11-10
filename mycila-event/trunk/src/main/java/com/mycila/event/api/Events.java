package com.mycila.event.api;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.mycila.event.api.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Events {

    private Events() {
    }

    public static <E> Event<E> event(final Topic topic, final E source) {
        notNull(topic, "Topic");
        notNull(source, "Source");
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

            @Override
            public String toString() {
                return "Event{timestamp=" + timestamp + ",topic=" + topic + ",type=" + source.getClass().getName() + "}";
            }
        };
    }

    public static <E> VetoableEvent<E> vetoable(final Topic topic, final E source) {
        return vetoable(event(topic, source));
    }

    public static <E> VetoableEvent<E> vetoable(final Event<E> event) {
        notNull(event, "Event");
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

            @Override
            public String toString() {
                return "VetoableEvent{timestamp=" + event.timestamp() + ",topic=" + event.topic() + ",type=" + event.source().getClass().getName() + ",allowed=" + allowed + "}";
            }
        };
    }

}
