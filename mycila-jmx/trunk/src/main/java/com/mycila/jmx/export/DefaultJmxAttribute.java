package com.mycila.jmx.export;

import javax.management.InvalidAttributeValueException;
import javax.management.ReflectionException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class DefaultJmxAttribute implements JmxAttribute {
    @Override
    public String getName() {
        return null;
    }

    @Override
    public Object get(Object object) throws ReflectionException {
        return null;
    }

    @Override
    public void set(Object managedResource, Object value) throws InvalidAttributeValueException, ReflectionException {
    }
}
