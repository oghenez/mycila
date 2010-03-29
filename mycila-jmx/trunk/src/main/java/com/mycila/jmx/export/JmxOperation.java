package com.mycila.jmx.export;

import javax.management.ReflectionException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface JmxOperation {
    Object invoke(Object o, Object... params) throws ReflectionException;
}