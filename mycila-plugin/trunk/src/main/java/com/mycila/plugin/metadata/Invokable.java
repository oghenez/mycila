package com.mycila.plugin.metadata;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Invokable<T> {
    T invoke(Object... args) throws InvokeException;
}
