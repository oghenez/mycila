package com.mycila.event.api.subscriber;

import com.mycila.event.api.event.Event;
import com.mycila.event.api.util.Listener;
import com.mycila.event.api.util.WeakReferencable;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Subscriber<E> extends WeakReferencable, Listener<E> {
    void onEvent(Event<E> event) throws Exception;
}
