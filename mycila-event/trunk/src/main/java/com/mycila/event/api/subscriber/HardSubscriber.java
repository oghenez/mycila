package com.mycila.event.api.subscriber;

import com.mycila.event.api.util.ref.Reachability;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class HardSubscriber<E> implements Subscriber<E> {
    @Override
    public final Reachability reachability() {
        return Reachability.HARD;
    }
}
