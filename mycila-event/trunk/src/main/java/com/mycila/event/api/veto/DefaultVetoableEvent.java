package com.mycila.event.api.veto;

import com.mycila.event.api.topic.Topic;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DefaultVetoableEvent<E> implements VetoableEvent<E> {

    private final AtomicBoolean allowed = new AtomicBoolean(true);
    private final Topic topic;
    private final E event;

    public DefaultVetoableEvent(Topic topic, E event) {
        this.event = event;
        this.topic = topic;
    }

    @Override
    public E event() {
        return event;
    }

    @Override
    public Topic topic() {
        return topic;
    }

    @Override
    public void veto() {
        allowed.set(false);
    }

    @Override
    public boolean isAllowed() {
        return allowed.get();
    }
}
