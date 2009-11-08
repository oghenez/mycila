package com.mycila.event.subscriber;

import com.mycila.event.WeakReferencable;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Subscriber<E> extends WeakReferencable {
    void onEvent(E event) throws Exception;
}
