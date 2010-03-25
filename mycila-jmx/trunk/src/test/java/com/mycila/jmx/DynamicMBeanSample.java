package com.mycila.jmx;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class DynamicMBeanSample {

    public static void main(String[] args) {

    }

    private static final class DynaBean implements DynamicMBean {

        Object object;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        MBeanInfo mBeanInfo = new MBeanInfo(getObject().getClass().getCanonicalName(),
                description,
                attrInfo,
                null,
                opInfo,
                null);

        DynaBean(Object object) {
            this.object = object;

        }

        public Object getAttribute(String attribute) throws AttributeNotFoundException, MBeanException, ReflectionException {
            ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(classLoader);
                //TODO
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
                //TODO
            }
            finally {
                Thread.currentThread().setContextClassLoader(currentClassLoader);
            }
        }

        public AttributeList setAttributes(AttributeList attributes) {
            ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(classLoader);
                //TODO
            }
            finally {
                Thread.currentThread().setContextClassLoader(currentClassLoader);
            }
        }

        public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
            ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(classLoader);
                //TODO
            }
            finally {
                Thread.currentThread().setContextClassLoader(currentClassLoader);
            }
        }

        public MBeanInfo getMBeanInfo() {
            return null;
        }
    }

}
