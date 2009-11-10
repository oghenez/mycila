package com.mycila.event.api;

import com.mycila.event.api.VetoableEvent;
import com.mycila.event.api.util.Listener;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Vetoer<E> extends Listener<E> {
    void check(VetoableEvent<E> event);
}
