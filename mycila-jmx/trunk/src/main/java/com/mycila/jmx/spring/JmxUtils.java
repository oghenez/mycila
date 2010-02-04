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

package com.mycila.jmx.spring;

import javax.management.DynamicMBean;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MXBean;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.beans.PropertyDescriptor;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.List;

/**
 * Collection of generic utility methods to support Spring JMX.
 * Includes a convenient method to locate an MBeanServer.
 *
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @see #locateMBeanServer
 * @since 1.2
 */
public abstract class JmxUtils {

    /**
     * The key used when extending an existing {@link javax.management.ObjectName} with the
     * identity hash code of its corresponding managed resource.
     */
    public static final String IDENTITY_OBJECT_NAME_KEY = "identity";

    /**
     * Suffix used to identify an MBean interface.
     */
    private static final String MBEAN_SUFFIX = "MBean";

    /**
     * Suffix used to identify a Java 6 MXBean interface.
     */
    private static final String MXBEAN_SUFFIX = "MXBean";

    private static final String MXBEAN_ANNOTATION_CLASS_NAME = "javax.management.MXBean";


    private static final boolean mxBeanAnnotationAvailable =
            ClassUtils.isPresent(MXBEAN_ANNOTATION_CLASS_NAME, JmxUtils.class.getClassLoader());


    /**
     * Attempt to find a locally running <code>MBeanServer</code>. Fails if no
     * <code>MBeanServer</code> can be found. Logs a warning if more than one
     * <code>MBeanServer</code> found, returning the first one from the list.
     *
     * @return the <code>MBeanServer</code> if found
     *         if no <code>MBeanServer</code> could be found
     * @see javax.management.MBeanServerFactory#findMBeanServer
     */
    public static MBeanServer locateMBeanServer() throws MBeanServerNotFoundException {
        return locateMBeanServer(null);
    }

    /**
     * Attempt to find a locally running <code>MBeanServer</code>. Fails if no
     * <code>MBeanServer</code> can be found. Logs a warning if more than one
     * <code>MBeanServer</code> found, returning the first one from the list.
     *
     * @param agentId the agent identifier of the MBeanServer to retrieve.
     *                If this parameter is <code>null</code>, all registered MBeanServers are considered.
     *                If the empty String is given, the platform MBeanServer will be returned.
     * @return the <code>MBeanServer</code> if found
     * @throws com.mycila.jmx.spring.MBeanServerNotFoundException
     *          if no <code>MBeanServer</code> could be found
     * @see javax.management.MBeanServerFactory#findMBeanServer(String)
     */
    public static MBeanServer locateMBeanServer(String agentId) throws MBeanServerNotFoundException {
        MBeanServer server = null;

        // null means any registered server, but "" specifically means the platform server
        if (!"".equals(agentId)) {
            List<MBeanServer> servers = MBeanServerFactory.findMBeanServer(agentId);
            if (servers != null && servers.size() > 0) {

                server = servers.get(0);
            }
        }

        if (server == null && !StringUtils.hasLength(agentId)) {
            // Attempt to load the PlatformMBeanServer.
            try {
                server = ManagementFactory.getPlatformMBeanServer();
            }
            catch (SecurityException ex) {
                throw new MBeanServerNotFoundException("No specific MBeanServer found, " +
                        "and not allowed to obtain the Java platform MBeanServer", ex);
            }
        }

        if (server == null) {
            throw new MBeanServerNotFoundException(
                    "Unable to locate an MBeanServer instance" +
                            (agentId != null ? " with agent id [" + agentId + "]" : ""));
        }

        return server;
    }

    /**
     * Convert an array of <code>MBeanParameterInfo</code> into an array of
     * <code>Class</code> instances corresponding to the parameters.
     *
     * @param paramInfo the JMX parameter info
     * @return the parameter types as classes
     * @throws ClassNotFoundException if a parameter type could not be resolved
     */
    public static Class[] parameterInfoToTypes(MBeanParameterInfo[] paramInfo) throws ClassNotFoundException {
        return parameterInfoToTypes(paramInfo, ClassUtils.getDefaultClassLoader());
    }

    /**
     * Convert an array of <code>MBeanParameterInfo</code> into an array of
     * <code>Class</code> instances corresponding to the parameters.
     *
     * @param paramInfo   the JMX parameter info
     * @param classLoader the ClassLoader to use for loading parameter types
     * @return the parameter types as classes
     * @throws ClassNotFoundException if a parameter type could not be resolved
     */
    public static Class[] parameterInfoToTypes(MBeanParameterInfo[] paramInfo, ClassLoader classLoader)
            throws ClassNotFoundException {

        Class[] types = null;
        if (paramInfo != null && paramInfo.length > 0) {
            types = new Class[paramInfo.length];
            for (int x = 0; x < paramInfo.length; x++) {
                types[x] = ClassUtils.forName(paramInfo[x].getType(), classLoader);
            }
        }
        return types;
    }

    /**
     * Create a <code>String[]</code> representing the argument signature of a
     * method. Each element in the array is the fully qualified class name
     * of the corresponding argument in the methods signature.
     *
     * @param method the method to build an argument signature for
     * @return the signature as array of argument types
     */
    public static String[] getMethodSignature(Method method) {
        Class[] types = method.getParameterTypes();
        String[] signature = new String[types.length];
        for (int x = 0; x < types.length; x++) {
            signature[x] = types[x].getName();
        }
        return signature;
    }

    /**
     * Return the JMX attribute name to use for the given JavaBeans property.
     * <p>When using strict casing, a JavaBean property with a getter method
     * such as <code>getFoo()</code> translates to an attribute called
     * <code>Foo</code>. With strict casing disabled, <code>getFoo()</code>
     * would translate to just <code>foo</code>.
     *
     * @param property        the JavaBeans property descriptor
     * @param useStrictCasing whether to use strict casing
     * @return the JMX attribute name to use
     */
    public static String getAttributeName(PropertyDescriptor property, boolean useStrictCasing) {
        if (useStrictCasing) {
            return StringUtils.capitalize(property.getName());
        } else {
            return property.getName();
        }
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
     * Return the class or interface to expose for the given bean.
     * This is the class that will be searched for attributes and operations
     * (for example, checked for annotations).
     * <p>This implementation returns the superclass for a CGLIB proxy and
     * the class of the given bean else (for a JDK proxy or a plain bean class).
     *
     * @param managedBean the bean instance (might be an AOP proxy)
     * @return the bean class to expose
     */
    public static Class getClassToExpose(Object managedBean) {
        return ClassUtils.getUserClass(managedBean);
    }

    /**
     * Return the class or interface to expose for the given bean class.
     * This is the class that will be searched for attributes and operations
     * (for example, checked for annotations).
     * <p>This implementation returns the superclass for a CGLIB proxy and
     * the class of the given bean else (for a JDK proxy or a plain bean class).
     *
     * @param beanClass the bean class (might be an AOP proxy class)
     * @return the bean class to expose
     */
    public static Class getClassToExpose(Class beanClass) {
        return ClassUtils.getUserClass(beanClass);
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
     * Check whether MXBean support is available, i.e. whether we're running
     * on Java 6 or above.
     *
     * @return <code>true</code> if available; <code>false</code> otherwise
     */
    public static boolean isMXBeanSupportAvailable() {
        return mxBeanAnnotationAvailable;
    }


    /**
     * Inner class to avoid a Java 6 dependency.
     */
    private static class MXBeanChecker {

        public static Boolean evaluateMXBeanAnnotation(Class<?> iface) {
            MXBean mxBean = iface.getAnnotation(MXBean.class);
            return (mxBean != null ? mxBean.value() : null);
        }
    }

}