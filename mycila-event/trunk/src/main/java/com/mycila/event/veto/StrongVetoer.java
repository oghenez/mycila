package com.mycila.event.veto;

import com.mycila.event.WeakReferencable;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class StrongVetoer<E> implements Vetoer<E>, WeakReferencable {
    @Override
    public final boolean isWeak() {
        return false;
    }
}