package com.mycila.jmx.export;

import javax.management.AttributeNotFoundException;
import javax.management.ImmutableDescriptor;
import javax.management.modelmbean.ModelMBeanInfoSupport;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DefaultJmxMetadata implements JmxMetadata {

    private final Class<?> clazz;
    private String description = "";
    private boolean mutable;

    public DefaultJmxMetadata(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public JmxAttribute getAttribute(String attribute) throws AttributeNotFoundException {
        return null;
    }

    @Override
    public JmxOperation getOperation(String operation, Class<?>... paramTypes) throws OperationNotFoundException {
        return null;
    }

    @Override
    public ModelMBeanInfoSupport getMBeanInfo() {
        return mutable ?
                new ModelMBeanInfoSupport(clazz.getName(), description, null, null, null, null, new ImmutableDescriptor("immutableInfo=true")) :
                new ModelMBeanInfoSupport(clazz.getName(), description, null, null, null, null);
    }

    public DefaultJmxMetadata withDescription(String description) {
        this.description = description;
        return this;
    }

    public DefaultJmxMetadata isMutable() {
        this.mutable = true;
        return this;
    }
}
