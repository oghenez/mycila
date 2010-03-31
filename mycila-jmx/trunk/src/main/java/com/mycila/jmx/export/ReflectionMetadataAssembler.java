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
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class ReflectionMetadataAssembler extends MetadataAssemblerSkeleton {
    @Override
    protected Collection<Field> getAttributes(Class<?> managedClass) {
        Set<Field> fields = new HashSet<Field>();
        while (managedClass != null && !managedClass.equals(Object.class)) {
            for (Field field : managedClass.getFields())
                if (Modifier.isPublic(field.getModifiers()) && !field.isSynthetic())
                    fields.add(field);
            managedClass = managedClass.getSuperclass();
        }
        return fields;
    }

    @Override
    protected String getAttributeExportName(Class<?> managedClass, Field attribute) {
        return ClassUtils.getShortName(attribute.getDeclaringClass()) + "." + attribute.getName();
    }

    @Override
    protected String getAttributeDescription(Class<?> managedClass, Field attribute) {
        return "Field";
    }

    @Override
    protected Collection<BeanProperty> getProperties(Class<?> managedClass) {
        Map<PropertyDescriptor, BeanProperty> methods = new HashMap<PropertyDescriptor, BeanProperty>();
        //TODO
        /*PropertyDescriptor[] desc;
        try {
            desc = Introspector.getBeanInfo(managedClass).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        for (PropertyDescriptor prop : desc) {
            Method[] accessors = new Method[]{prop.getReadMethod(), prop.getWriteMethod()};
            if (accessors[0] != null
                    && accessors[0].getDeclaringClass() == Object.class
                    || accessors[0] == null && accessors[1] == null)
                continue;

            //TODO: JmxProperty class

            // If both getter and setter are null, then this does not need exposing.
            String attrName = JmxUtils.getAttributeName(prop, true);

            String description = getAttributeDescription(prop, beanKey);
            ModelMBeanAttributeInfo info = new ModelMBeanAttributeInfo(attrName, description, getter, setter);

            Descriptor desc = info.getDescriptor();
            if (getter != null) {
                desc.setField(FIELD_GET_METHOD, getter.getName());
            }
            if (setter != null) {
                desc.setField(FIELD_SET_METHOD, setter.getName());
            }

            populateAttributeDescriptor(desc, getter, setter, beanKey);
            info.setDescriptor(desc);
            infos.add(info);

        }


        for (Method method : managedClass.getMethods()) {
            if (Object.class.equals(method.getDeclaringClass())
                    || method.isSynthetic() || method.isBridge())
                continue;
            boolean isGetter = ClassUtils.isGetter(method);
            boolean isSetter = ClassUtils.isSetter(method);
            if (isGetter || isSetter) {
                String property = JmxUtils.getProperty(method);
                if (isGetter)
                    property = method

                Method[] pair = methods.get(property);
                if (pair == null) methods.put(property, pair = new Method[2]);
                if (isGetter) pair[0] = method;
                if (isSetter) pair[1] = method;
            }
        }*/
        return methods.values();
    }

    @Override
    protected String getPropertyDescription(Class<?> managedClass, BeanProperty property) {
        return "Property";
    }

    @Override
    protected Collection<Method> getMethodOperations(Class<?> managedClass) {
        Set<Method> methods = new HashSet<Method>();
        for (Method method : managedClass.getMethods()) {
            if (Object.class.equals(method.getDeclaringClass())
                    || method.isSynthetic() || method.isBridge())
                continue;
            methods.add(method);
        }
        return methods;
    }

    @Override
    protected String getOperationDescription(Class<?> managedClass, Method operation) {
        return "Operation";
    }
}