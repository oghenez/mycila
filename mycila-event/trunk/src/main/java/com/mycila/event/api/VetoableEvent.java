package com.mycila.event.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface VetoableEvent<E> extends Vetoable {
    Destination destination();
    E event();
}
