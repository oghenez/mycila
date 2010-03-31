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
import com.mycila.jmx.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
                if (canInclude(managedClass, field))
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
        Map<String, BeanProperty> properties = new HashMap<String, BeanProperty>();
        Collection<Method> methods = getMethodOperations(managedClass);
        for (Method method : methods) {
            BeanProperty prop = BeanProperty.findProperty(managedClass, method);
            if (prop != null && !properties.containsKey(prop.getName()) && canInclude(managedClass, prop))
                properties.put(prop.getName(), prop);
        }
        return properties.values();
    }

    @Override
    protected String getPropertyDescription(Class<?> managedClass, BeanProperty property) {
        return "Property";
    }

    @Override
    protected Collection<Method> getMethodOperations(Class<?> managedClass) {
        List<Method> methods = new ArrayList<Method>();
        for (Method method : ReflectionUtils.getMethods(managedClass))
            if (canInclude(managedClass, method))
                methods.add(method);
        return methods;
    }

    @Override
    protected String getOperationDescription(Class<?> managedClass, Method operation) {
        return "Operation";
    }

    protected boolean canInclude(Class<?> managedClass, BeanProperty property) {
        return property.getReadMethod() == null || !property.getReadMethod().getDeclaringClass().equals(Object.class);
    }

    protected boolean canInclude(Class<?> managedClass, Method method) {
        return !(method.isSynthetic() || method.isBridge() || Object.class.equals(method.getDeclaringClass()));
    }

    protected boolean canInclude(Class<?> managedClass, Field field) {
        return Modifier.isPublic(field.getModifiers()) && !field.isSynthetic();
    }
}