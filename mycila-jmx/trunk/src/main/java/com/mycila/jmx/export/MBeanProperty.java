/**
 * Copyright (C) 2010 Mathieu Carbou <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mycila.jmx.export;

import com.mycila.jmx.util.ClassUtils;

import javax.management.IntrospectionException;
import javax.management.InvalidAttributeValueException;
import javax.management.ReflectionException;
import javax.management.modelmbean.ModelMBeanAttributeInfo;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class MBeanProperty<T> implements JmxAttribute<T> {

    private final BeanProperty<T> beanProperty;
    private final ModelMBeanAttributeInfo attributeInfo;

    public MBeanProperty(BeanProperty<T> beanProperty, String exportName, String description, Access access) {
        this.beanProperty = beanProperty;
        try {
            this.attributeInfo = new ModelMBeanAttributeInfo(
                    exportName,
                    description,
                    access == Access.RO || access == Access.RW ? beanProperty.getReadMethod() : null,
                    access == Access.WO || access == Access.RW ? beanProperty.getWriteMethod() : null);
        } catch (IntrospectionException e) {
            throw new RuntimeException("Error creating property from " + beanProperty + ": " + e.getMessage(), e);
        }
    }

    @Override
    public String getName() {
        return getMetadata().getName();
    }

    @Override
    public ModelMBeanAttributeInfo getMetadata() {
        return attributeInfo;
    }

    @Override
    public T get(Object managedResource) throws ReflectionException {
        if (!getMetadata().isReadable())
            throw new ReflectionException(new IllegalAccessException("Attribute not readable: " + this));
        try {
            return beanProperty.get(managedResource);
        } catch (Throwable e) {
            if (e instanceof Exception)
                throw new ReflectionException((Exception) e, "Error getting attribute " + this);
            throw new ReflectionException(new Exception(e), "Error getting attribute " + this);
        }
    }

    @Override
    public void set(Object managedResource, T value) throws InvalidAttributeValueException, ReflectionException {
        if (!getMetadata().isWritable())
            throw new ReflectionException(new IllegalAccessException("Attribute not writable: " + this));
        if (!ClassUtils.isAssignableValue(beanProperty.getType(), value))
            throw new InvalidAttributeValueException("Invalid type specified for " + this + ": " + value);
        try {
            beanProperty.set(managedResource, beanProperty.getType().cast(value));
        } catch (Throwable e) {
            if (e instanceof Exception)
                throw new ReflectionException((Exception) e, "Error setting attribute " + this);
            throw new ReflectionException(new Exception(e), "Error setting attribute " + this);
        }
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MBeanProperty that = (MBeanProperty) o;
        return beanProperty.equals(that.beanProperty);
    }

    @Override
    public int hashCode() {
        return beanProperty.hashCode();
    }
}