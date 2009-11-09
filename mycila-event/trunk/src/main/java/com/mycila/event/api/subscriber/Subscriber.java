package com.mycila.event.api.subscriber;

import com.mycila.event.api.event.Event;
import com.mycila.event.api.util.Listener;
import com.mycila.event.api.util.ref.Referencable;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Subscriber<E> extends Referencable, Listener<E> {
    void onEvent(Event<E> event) throws Exception;
}
