package com.mycila.event.api;

import com.mycila.event.api.annotation.Reference;
import com.mycila.event.api.Event;
import static com.mycila.event.api.ref.Reachability.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@Reference(HARD)
public interface Subscriber<E> {
    void onEvent(Event<E> event) throws Exception;
}
