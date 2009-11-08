package com.mycila.event.subscriber;

import com.mycila.event.WeakReferencable;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class WeakSubscriber<E> implements Subscriber<E>, WeakReferencable {
    @Override
    public final boolean isWeak() {
        return true;
    }
}