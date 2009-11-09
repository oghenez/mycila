package com.mycila.event.api.veto;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class WeakVetoer<E> implements Vetoer<E> {
    @Override
    public final boolean isWeak() {
        return true;
    }
}