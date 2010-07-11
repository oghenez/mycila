package com.mycila.plugin;

import com.mycila.plugin.metadata.InvokeException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Invokable<T> {
    T invoke(Object... args) throws InvokeException;
}
