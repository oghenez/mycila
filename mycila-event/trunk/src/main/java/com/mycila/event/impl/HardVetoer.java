package com.mycila.event.impl;

import com.mycila.event.api.Reachability;
import com.mycila.event.api.Vetoer;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class HardVetoer<E> implements Vetoer<E> {
    @Override
    public final Reachability reachability() {
        return Reachability.HARD;
    }
}