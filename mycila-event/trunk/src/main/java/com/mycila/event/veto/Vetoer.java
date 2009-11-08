package com.mycila.event.veto;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Vetoer<E> {
    void check(VetoableEvent<E> event);
}
