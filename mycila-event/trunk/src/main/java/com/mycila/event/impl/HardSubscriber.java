package com.mycila.event.impl;

import com.mycila.event.api.Reachability;
import com.mycila.event.api.Subscriber;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class HardSubscriber<E> implements Subscriber<E> {
    @Override
    public final Reachability reachability() {
        return Reachability.HARD;
    }
}
