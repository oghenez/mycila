package com.mycila.event.api.veto;

import com.mycila.event.api.event.VetoableEvent;
import com.mycila.event.api.util.Listener;
import com.mycila.event.api.util.ref.Referencable;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Vetoer<E> extends Referencable, Listener<E> {
    void check(VetoableEvent<E> event);
}
