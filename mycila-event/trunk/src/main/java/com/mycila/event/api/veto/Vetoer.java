package com.mycila.event.api.veto;

import com.mycila.event.api.util.Listener;
import com.mycila.event.api.util.WeakReferencable;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Vetoer<E> extends WeakReferencable, Listener<E> {
    void check(VetoableEvent<E> event);
}
