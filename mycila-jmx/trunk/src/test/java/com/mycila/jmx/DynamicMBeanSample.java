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
import javax.management.modelmbean.DescriptorSupport;
import javax.management.modelmbean.XMLParseException;
import java.util.concurrent.CountDownLatch;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DynamicMBeanSample {

    public static void main(String[] args) throws Exception {
        MyObject object = new MyObject();
        DynaBean mbean = new DynaBean(object);
        new JmxServerFactory().locateDefault().registerMBean(mbean, new ObjectName(object.getClass().getPackage().getName() + ":type=" + object.getClass().getSimpleName()));
        new CountDownLatch(1).await();
    }

    public static class MyObject {

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
            return mBeanInfo;
        }
    }

}
