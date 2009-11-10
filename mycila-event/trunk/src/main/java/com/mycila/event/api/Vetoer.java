package com.mycila.event.api;

import com.mycila.event.api.annotation.Reference;

import static com.mycila.event.api.Reachability.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Reference(HARD)
public interface Vetoer<E> {
    void check(VetoableEvent<E> event);
}
