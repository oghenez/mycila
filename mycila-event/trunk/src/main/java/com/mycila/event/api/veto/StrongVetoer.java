package com.mycila.event.api.veto;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class StrongVetoer<E> implements Vetoer<E> {
    @Override
    public final boolean isWeak() {
        return false;
    }
}