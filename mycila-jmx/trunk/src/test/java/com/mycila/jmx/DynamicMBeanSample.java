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

package com.mycila.jmx;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.ImmutableDescriptor;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.StandardMBean;
import java.util.concurrent.CountDownLatch;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DynamicMBeanSample {

    public static void main(String[] args) throws Exception {
        MyObject object = new MyObject();
        DynaBean mbean = new DynaBean(object);
        new JmxServerFactory().locateDefault().registerMBean(mbean, new ObjectName("com:type=a"));

        MyObject2 object2 = new MyObject2();
        new JmxServerFactory().locateDefault().registerMBean(new StandardMBean(new MyObject2(), MyObject2MBean.class), new ObjectName("com:type=b"));

        new CountDownLatch(1).await();
    }

    public static class MyObject {
    }

    public static interface MyObject2MBean {
    }

    public static class MyObject2 implements MyObject2MBean {
    }

    private static final class DynaBean implements DynamicMBean {

        Object object;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        DynaBean(Object object) {
            this.object = object;
        }

        public Object getAttribute(String attribute) throws AttributeNotFoundException, MBeanException, ReflectionException {
            ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(classLoader);
                return null;//TODO
            }
            finally {
                Thread.currentThread().setContextClassLoader(currentClassLoader);
            }
        }

        public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
            ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(classLoader);
                //TODO
            }
            finally {
                Thread.currentThread().setContextClassLoader(currentClassLoader);
            }
        }

        public AttributeList getAttributes(String[] attributes) {
            ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(classLoader);
                return null;//TODO
            }
            finally {
                Thread.currentThread().setContextClassLoader(currentClassLoader);
            }
        }

        public AttributeList setAttributes(AttributeList attributes) {
            ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(classLoader);
                return null;//TODO
            }
            finally {
                Thread.currentThread().setContextClassLoader(currentClassLoader);
            }
        }

        public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
            ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(classLoader);
                return null;//TODO
            }
            finally {
                Thread.currentThread().setContextClassLoader(currentClassLoader);
            }
        }

        public MBeanInfo getMBeanInfo() {
            MBeanInfo mBeanInfo = new MBeanInfo(object.getClass().getName(), "my desc", null, null, null, null, new ImmutableDescriptor("immutableInfo=true"));
            //mBeanInfo.getDescriptor().setField("immutableInfo", true);
            //interfaceClassName
            // isMXBean
            return mBeanInfo;
        }
    }

}
