package com.mycila.event.api.veto;

import com.mycila.event.api.util.ref.Reachability;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class HardVetoer<E> implements Vetoer<E> {
    @Override
    public final Reachability reachability() {
        return Reachability.HARD;
    }
}