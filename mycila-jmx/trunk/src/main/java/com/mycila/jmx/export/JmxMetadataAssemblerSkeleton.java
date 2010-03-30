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
import com.mycila.jmx.util.JmxUtils;

import javax.management.Descriptor;
import javax.management.MBeanParameterInfo;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class JmxMetadataAssemblerSkeleton implements JmxMetadataAssembler {
    @Override
    public JmxMetadata getMetadata(Class<?> managedClass) {
        return new MBeanMetadata(
                managedClass.getName(),
                getMBeanDescription(managedClass),
                getMBeanAttributes(managedClass),
                getMBeanOperations(managedClass));
    }

    protected String getMBeanDescription(Class<?> managedClass) {
        return managedClass.getName();
    }

    protected Collection<JmxAttribute> getMBeanAttributes(Class<?> managedClass) {
        List<JmxAttribute> jmxAttributes = new LinkedList<JmxAttribute>();
        for (Method[] accessors : getProperties(managedClass)) {
            if (accessors.length != 2 || accessors[0] == null && accessors[1] == null)
                throw new IllegalStateException("getMethodAttributes(managedClass) must return an array of two methods [getter, setter] with at least one method which is non null");
            jmxAttributes.add(buildProperty(managedClass, accessors[0], accessors[1]));
        }
        for (Field field : getAttributes(managedClass))
            jmxAttributes.add(buildAttribute(managedClass, field));
        return jmxAttributes;
    }

    protected JmxAttribute buildAttribute(Class<?> managedClass, Field field) {
        FieldJmxAttribute jmxAttribute = new FieldJmxAttribute(
                field,
                getAttributeExportName(managedClass, field),
                getAttributeDescription(managedClass, field),
                getAttributeAccess(managedClass, field));
        Descriptor desc = jmxAttribute.getMetadata().getDescriptor();
        populateAttributeDescriptor(managedClass, field, desc);
        jmxAttribute.getMetadata().setDescriptor(desc);
        return jmxAttribute;
    }

    protected JmxAttribute buildProperty(Class<?> managedClass, Method getter, Method setter) {
        MethodAttribute jmxAttribute = new MethodAttribute(
                getter, setter,
                getPropertyExportName(managedClass, getter, setter),
                getPropertyDescription(managedClass, getter, setter));
        Descriptor desc = jmxAttribute.getMetadata().getDescriptor();
        populatePropertyDescriptor(managedClass, getter, setter, desc);
        jmxAttribute.getMetadata().setDescriptor(desc);
        return jmxAttribute;
    }

    protected Collection<JmxOperation> getMBeanOperations(Class<?> managedClass) {
        List<JmxOperation> jmxOperations = new LinkedList<JmxOperation>();
        for (Method method : getMethodOperations(managedClass))
            jmxOperations.add(buildOperation(managedClass, method));
        return jmxOperations;
    }

    protected JmxOperation buildOperation(Class<?> managedClass, Method operation) {
        MethodOperation jmxOperation = new MethodOperation(
                operation,
                getOperationExportName(managedClass, operation),
                getOperationDescription(managedClass, operation),
                getOperationParameters(managedClass, operation));
        Descriptor desc = jmxOperation.getMetadata().getDescriptor();
        populateOperationDescriptor(managedClass, operation, desc);
        jmxOperation.getMetadata().setDescriptor(desc);
        return jmxOperation;
    }

    // field attributes

    protected abstract Collection<Field> getAttributes(Class<?> managedClass);

    protected String getAttributeExportName(Class<?> managedClass, Field attribute) {
        return attribute.getName();
    }

    protected String getAttributeDescription(Class<?> managedClass, Field attribute) {
        return attribute.getName();
    }

    protected Access getAttributeAccess(Class<?> managedClass, Field attribute) {
        return Modifier.isFinal(attribute.getModifiers()) ? Access.RO : Access.RW;
    }

    protected void populateAttributeDescriptor(Class<?> managedClass, Field attribute, Descriptor desc) {
        JmxUtils.populateDeprecation(desc, attribute);
        JmxUtils.populateEnable(desc, true);
        JmxUtils.populateDisplayName(desc, attribute.getName());
        JmxUtils.populateVisibility(desc, 1);
    }

    // method attributes

    protected abstract Collection<Method[]> getProperties(Class<?> managedClass);

    protected String getPropertyExportName(Class<?> managedClass, Method getter, Method setter) {
        return getter != null ? JmxUtils.getProperty(getter) : JmxUtils.getProperty(setter);
    }

    protected String getPropertyDescription(Class<?> managedClass, Method getter, Method setter) {
        return getter != null ? JmxUtils.getProperty(getter) : JmxUtils.getProperty(setter);
    }

    protected void populatePropertyDescriptor(Class<?> managedClass, Method getter, Method setter, Descriptor desc) {
        JmxUtils.populateDeprecation(desc, getter);
        JmxUtils.populateDeprecation(desc, setter);
        JmxUtils.populateEnable(desc, true);
        JmxUtils.populateDisplayName(desc, getter != null ? JmxUtils.getProperty(getter) : JmxUtils.getProperty(setter));
        JmxUtils.populateVisibility(desc, 1);
        JmxUtils.populateAccessors(desc, getter, setter);
    }

    // method operations

    protected abstract Collection<Method> getMethodOperations(Class<?> managedClass);

    protected String getOperationExportName(Class<?> managedClass, Method operation) {
        return operation.getName();
    }

    protected String getOperationDescription(Class<?> managedClass, Method operation) {
        return operation.getName();
    }

    private MBeanParameterInfo[] getOperationParameters(Class<?> managedClass, Method operation) {
        Class<?>[] paramTypes = operation.getParameterTypes();
        MBeanParameterInfo[] params = new MBeanParameterInfo[paramTypes.length];
        for (int i = 0; i < params.length; i++)
            params[i] = new MBeanParameterInfo(ClassUtils.getShortName(paramTypes[i]), paramTypes[i].getName(), "");
        return params;
    }

    protected void populateOperationDescriptor(Class<?> managedClass, Method operation, Descriptor desc) {
        JmxUtils.populateDeprecation(desc, operation);
        JmxUtils.populateEnable(desc, true);
        JmxUtils.populateDisplayName(desc, operation.getName());
        JmxUtils.populateVisibility(desc, 1);
        if (ClassUtils.isGetter(operation))
            JmxUtils.populateRole(desc, Role.GETTER);
        else if (ClassUtils.isSetter(operation))
            JmxUtils.populateRole(desc, Role.SETTER);
        else
            JmxUtils.populateRole(desc, Role.OPERATION);
    }

}
