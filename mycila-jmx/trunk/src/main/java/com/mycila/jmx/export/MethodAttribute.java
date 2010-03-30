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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class MethodAttribute implements JmxAttribute {

    private final Method getter;
    private final Method setter;
    private final ModelMBeanAttributeInfo attributeInfo;

    public MethodAttribute(Method getter, Method setter, String exportName, String description) {
        this.getter = getter;
        this.setter = setter;
        try {
            this.attributeInfo = new ModelMBeanAttributeInfo(
                    exportName, description,
                    getter, setter);
        } catch (IntrospectionException e) {
            throw new RuntimeException("Error creating property from getter " + getter + " and setter " + setter + ": " + e.getMessage(), e);
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
    public Object get(Object managedResource) throws ReflectionException {
        if (getter == null)
            throw new ReflectionException(new IllegalAccessException("Attribute not readable: " + this));
        if (!getter.isAccessible())
            getter.setAccessible(true);
        try {
            return getter.invoke(managedResource);
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e, "Error getting attribute " + this);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof Exception)
                throw new ReflectionException((Exception) e.getTargetException(), "Error getting attribute " + this);
            throw new ReflectionException(new Exception(e.getTargetException()), "Error getting attribute " + this);
        }
    }

    @Override
    public void set(Object managedResource, Object value) throws InvalidAttributeValueException, ReflectionException {
        if (setter == null)
            throw new ReflectionException(new IllegalAccessException("Attribute not writable: " + this));
        if (!setter.isAccessible())
            setter.setAccessible(true);
        if (!ClassUtils.isAssignableValue(setter.getParameterTypes()[0], value))
            throw new InvalidAttributeValueException("Invalid type specified for " + this + ": " + value);
        try {
            setter.invoke(managedResource, value);
        } catch (IllegalAccessException e) {
            throw new ReflectionException(e, "Error setting attribute " + this);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof Exception)
                throw new ReflectionException((Exception) e.getTargetException(), "Error setting attribute " + this);
            throw new ReflectionException(new Exception(e.getTargetException()), "Error setting attribute " + this);
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
        MethodAttribute that = (MethodAttribute) o;
        return !(getter != null ? !getter.equals(that.getter) : that.getter != null) && !(setter != null ? !setter.equals(that.setter) : that.setter != null);
    }

    @Override
    public int hashCode() {
        int result = getter != null ? getter.hashCode() : 0;
        result = 31 * result + (setter != null ? setter.hashCode() : 0);
        return result;
    }
}