package com.mycila.jmx;

import tmp.spring.ClassUtils;
import tmp.spring.ObjectNameManager;

import javax.management.DynamicMBean;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.util.Hashtable;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class JmxUtils {

    /**
     * Suffix used to identify an MBean interface.
     */
    private static final String MBEAN_SUFFIX = "MBean";

    /**
     * Suffix used to identify a Java 6 MXBean interface.
     */
    private static final String MXBEAN_SUFFIX = "MXBean";

    /**
     * The key used when extending an existing {@link javax.management.ObjectName} with the
     * identity hash code of its corresponding managed resource.
     */
    private static final String IDENTITY_OBJECT_NAME_KEY = "identity";

    private static final String MXBEAN_ANNOTATION_CLASS_NAME = "javax.management.MXBean";

    private static final boolean mxBeanAnnotationAvailable =
            ClassUtils.isPresent(MXBEAN_ANNOTATION_CLASS_NAME, JmxUtils.class.getClassLoader());

    private JmxUtils() {
    }

    /**
     * Append an additional key/value pair to an existing {@link javax.management.ObjectName} with the key being
     * the static value <code>identity</code> and the value being the identity hash code of the
     * managed resource being exposed on the supplied {@link javax.management.ObjectName}. This can be used to
     * provide a unique {@link javax.management.ObjectName} for each distinct instance of a particular bean or
     * class. Useful when generating {@link javax.management.ObjectName ObjectNames} at runtime for a set of
     * managed resources based on the template value supplied by a
     *
     * @param objectName      the original JMX ObjectName
     * @param managedResource the MBean instance
     * @return an ObjectName with the MBean identity added
     * @throws javax.management.MalformedObjectNameException
     *          in case of an invalid object name specification
     */
    public static ObjectName appendIdentityToObjectName(ObjectName objectName, Object managedResource)
            throws MalformedObjectNameException {

        Hashtable<String, String> keyProperties = objectName.getKeyPropertyList();
        keyProperties.put(IDENTITY_OBJECT_NAME_KEY, Integer.toHexString(System.identityHashCode(managedResource)));
        return ObjectNameManager.getInstance(objectName.getDomain(), keyProperties);
    }

    /**
     * Determine whether the given bean class qualifies as an MBean as-is.
     * <p>This implementation checks for {@link javax.management.DynamicMBean}
     * classes as well as classes with corresponding "*MBean" interface
     * (Standard MBeans) or corresponding "*MXBean" interface (Java 6 MXBeans).
     *
     * @param beanClass the bean class to analyze
     * @return whether the class qualifies as an MBean
     */
    public static boolean isMBean(Class beanClass) {
        return (beanClass != null &&
                (DynamicMBean.class.isAssignableFrom(beanClass) ||
                        (getMBeanInterface(beanClass) != null || getMXBeanInterface(beanClass) != null)));
    }

    /**
     * Return the Standard MBean interface for the given class, if any
     * (that is, an interface whose name matches the class name of the
     * given class but with suffix "MBean").
     *
     * @param clazz the class to check
     * @return the Standard MBean interface for the given class
     */
    public static Class getMBeanInterface(Class clazz) {
        if (clazz.getSuperclass() == null) {
            return null;
        }
        String mbeanInterfaceName = clazz.getName() + MBEAN_SUFFIX;
        Class[] implementedInterfaces = clazz.getInterfaces();
        for (Class iface : implementedInterfaces) {
            if (iface.getName().equals(mbeanInterfaceName)) {
                return iface;
            }
        }
        return getMBeanInterface(clazz.getSuperclass());
    }

    /**
     * Return the Java 6 MXBean interface exists for the given class, if any
     * (that is, an interface whose name ends with "MXBean" and/or
     * carries an appropriate MXBean annotation).
     *
     * @param clazz the class to check
     * @return whether there is an MXBean interface for the given class
     */
    public static Class getMXBeanInterface(Class clazz) {
        if (clazz.getSuperclass() == null) {
            return null;
        }
        Class[] implementedInterfaces = clazz.getInterfaces();
        for (Class iface : implementedInterfaces) {
            boolean isMxBean = iface.getName().endsWith(MXBEAN_SUFFIX);
            if (mxBeanAnnotationAvailable) {
                Boolean checkResult = MXBeanChecker.evaluateMXBeanAnnotation(iface);
                if (checkResult != null) {
                    isMxBean = checkResult;
                }
            }
            if (isMxBean) {
                return iface;
            }
        }
        return getMXBeanInterface(clazz.getSuperclass());
    }

    /**
     * Inner class to avoid a Java 6 dependency.
     */
    private static class MXBeanChecker {

        public static Boolean evaluateMXBeanAnnotation(Class<?> iface) {
            javax.management.MXBean mxBean = iface.getAnnotation(javax.management.MXBean.class);
            return (mxBean != null ? mxBean.value() : null);
        }
    }
}
