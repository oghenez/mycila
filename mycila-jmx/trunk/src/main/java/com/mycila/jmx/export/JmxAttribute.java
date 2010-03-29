package com.mycila.jmx.export;

import javax.management.InvalidAttributeValueException;
import javax.management.ReflectionException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface JmxAttribute {
    String getName();

    Object get(Object object) throws ReflectionException;

    void set(Object managedResource, Object value) throws InvalidAttributeValueException, ReflectionException;
}
