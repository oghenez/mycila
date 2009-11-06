package com.mycila.event.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface DestinationFactory<T extends Destination> {
    T create(String name);
}
