package com.mycila.event.api.subscriber;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class WeakSubscriber<E> implements Subscriber<E> {
    @Override
    public final boolean isWeak() {
        return true;
    }
}