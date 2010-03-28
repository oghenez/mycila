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

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;
import javax.management.RuntimeOperationsException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class DefaultDynamicMBean implements DynamicMBean {

    private final Object managedResource;
    private final MBeanInfo mBeanInfo;

    public DefaultDynamicMBean(Object managedResource, MBeanInfo mBeanInfo) {
        this.managedResource = managedResource;
        this.mBeanInfo = mBeanInfo;
    }

    @Override
    public Object getAttribute(String attribute) throws AttributeNotFoundException, MBeanException, ReflectionException {
        // validation from javax.management.modelmbean.RequiredModelMBean
        if (attribute == null)
            throw new RuntimeOperationsException(new IllegalArgumentException("attributeName must not be null"), "Exception occurred trying to get attribute of a RequiredModelMBean");
    }

    @Override
    public AttributeList getAttributes(String[] attributes) {
        // validation from javax.management.modelmbean.RequiredModelMBean
        if (attributes == null)
            throw new RuntimeOperationsException(new IllegalArgumentException("attributeNames must not be null"), "Exception occurred trying to get attributes of a RequiredModelMBean");
    }

    @Override
    public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
        // validation from javax.management.modelmbean.RequiredModelMBean
        if (attribute == null)
            throw new RuntimeOperationsException(new IllegalArgumentException("attribute must not be null"), "Exception occurred trying to set an attribute of a RequiredModelMBean");
    }

    @Override
    public AttributeList setAttributes(AttributeList attributes) {
        // validation from javax.management.modelmbean.RequiredModelMBean
        if (attributes == null)
            throw new RuntimeOperationsException(new IllegalArgumentException("attributes must not be null"), "Exception occurred trying to set attributes of a RequiredModelMBean");
    }

    @Override
    public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
        // validation from javax.management.modelmbean.RequiredModelMBean
        if (actionName == null)
            throw new RuntimeOperationsException(new IllegalArgumentException("Method name must not be null"), "An exception occurred while trying to invoke a method on a RequiredModelMBean");
    }

    @Override
    public MBeanInfo getMBeanInfo() {
        return mBeanInfo;
    }

    public Object getManagedResource() {
        return managedResource;
    }

}