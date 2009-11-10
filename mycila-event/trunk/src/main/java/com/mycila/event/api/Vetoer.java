package com.mycila.event.api;

import static com.mycila.event.api.ref.Reachability.*;
import com.mycila.event.api.annotation.Reference;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Reference(HARD)
public interface Vetoer<E> {
    void check(VetoableEvent<E> event);
}
