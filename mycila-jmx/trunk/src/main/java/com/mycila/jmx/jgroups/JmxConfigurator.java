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

package com.mycila.jmx.jgroups;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Bela Ban, Vladimir Blagojevic
 * @version $Id: JmxConfigurator.java,v 1.17 2009/10/20 13:05:20 belaban Exp $
 */
public class JmxConfigurator {

    public static void register(Object obj, MBeanServer server, String name)
            throws MBeanRegistrationException, MalformedObjectNameException {
        internalRegister(obj, server, name);
    }

    public static void unregister(Object obj, MBeanServer server, String name)
            throws MBeanRegistrationException, MalformedObjectNameException {
        internalUnregister(obj, server, name);
    }

    private static void internalRegister(Object obj, MBeanServer server, String name)
            throws MalformedObjectNameException, MBeanRegistrationException {

        if (obj == null)
            throw new IllegalArgumentException("Object being registered cannot be null");
        if (server == null)
            throw new IllegalArgumentException("MBean server used for registeration cannot be null");

        try {
            ObjectName objName = getObjectName(obj, name);
            ResourceDMBean res = new ResourceDMBean(obj);
            server.registerMBean(res, objName);
        } catch (InstanceAlreadyExistsException e) {
            /*if (log.isErrorEnabled()) {
                log.error("register MBean failed " + e.getMessage());
            }*/
            throw new MBeanRegistrationException(e, "The @MBean objectName is not unique");
        } catch (NotCompliantMBeanException e) {
            /*if (log.isErrorEnabled()) {
                log.error("register MBean failed " + e.getMessage());
            }*/
            throw new MBeanRegistrationException(e);
        }

    }

    private static void internalUnregister(Object obj, MBeanServer server, String name)
            throws MBeanRegistrationException {
        try {
            if (name != null && name.length() > 0) {
                server.unregisterMBean(new ObjectName(name));
            } else if (obj != null) {
                server.unregisterMBean(getObjectName(obj, null));
            } else {
                throw new MBeanRegistrationException(null,
                        "Cannot find MBean name from @MBean or passed in value");
            }
        } catch (InstanceNotFoundException infe) {
            /*if (log.isErrorEnabled()) {
                log.error("unregister MBean failed " + infe.getMessage());
            }*/
            throw new MBeanRegistrationException(infe);
        } catch (MalformedObjectNameException e) {
            /*if (log.isErrorEnabled()) {
                log.error("unregister MBean failed " + e.getMessage());
            }*/
            throw new MBeanRegistrationException(e);
        }
    }

    private static ObjectName getObjectName(Object obj, String name)
            throws MalformedObjectNameException {
        MBean resource = obj.getClass().getAnnotation(MBean.class);
        if (name != null && name.length() > 0) {
            return new ObjectName(name);
        } else if (resource.objectName() != null && resource.objectName().length() > 0) {
            return new ObjectName(resource.objectName());
        } else {
            throw new MalformedObjectNameException("Instance " + obj + " of a class "
                    + obj.getClass() + " does not have a valid object name");
        }
    }

    /**
     * Unregisters object_name and everything under it
     *
     * @param object_name
     */
    public static void unregister(MBeanServer server, String object_name) throws Exception {
        Set<ObjectName> mbeans = server.queryNames(new ObjectName(object_name), null);
        if (mbeans != null) {
            for (Iterator<ObjectName> it = mbeans.iterator(); it.hasNext();) {
                server.unregisterMBean(it.next());
            }
        }
    }

}