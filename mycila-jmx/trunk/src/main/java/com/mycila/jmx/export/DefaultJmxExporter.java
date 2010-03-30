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

import javax.management.DynamicMBean;
import javax.management.InstanceAlreadyExistsException;
import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.StandardMBean;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DefaultJmxExporter implements JmxExporter {

    private final MBeanServer mBeanServer;
    private final ExportBehavior exportBehavior;
    private final JmxNamingStrategy namingStrategy;
    private final JmxMetadataAssembler metadataAssembler;

    public DefaultJmxExporter(MBeanServer mBeanServer) {
        this(mBeanServer, ExportBehavior.FAIL_ON_EXISTING);
    }

    public DefaultJmxExporter(MBeanServer mBeanServer,
                              ExportBehavior exportBehavior) {
        this(mBeanServer, exportBehavior, new DefaultJmxNamingStrategy());
    }

    public DefaultJmxExporter(MBeanServer mBeanServer,
                              ExportBehavior exportBehavior,
                              JmxNamingStrategy namingStrategy) {
        this(mBeanServer, exportBehavior, namingStrategy, new AnnotationJmxMetadataAssembler());
    }

    public DefaultJmxExporter(MBeanServer mBeanServer,
                              ExportBehavior exportBehavior,
                              JmxNamingStrategy namingStrategy,
                              JmxMetadataAssembler metadataAssembler) {
        this.mBeanServer = mBeanServer;
        this.exportBehavior = exportBehavior;
        this.namingStrategy = namingStrategy;
        this.metadataAssembler = metadataAssembler;
    }

    /* IMPL */

    @Override
    public void unregister(ObjectName objectName) {
        if (getMBeanServer().isRegistered(objectName))
            doUnregister(objectName);
    }

    @Override
    public ObjectName register(Object managedResource) throws JmxExportException {
        try {
            ObjectName objectName = getNamingStrategy().getObjectName(managedResource);
            objectName = JmxUtils.appendIdentityToObjectName(objectName, managedResource);
            register(managedResource, objectName);
            return objectName;
        } catch (MalformedObjectNameException e) {
            throw new JmxExportException("Unable to generate ObjectName for MBean [" + managedResource + "]", e);
        }
    }

    @Override
    public void register(Object managedResource, ObjectName objectName) throws JmxExportException {
        if (JmxUtils.isMBean(managedResource.getClass()))
            doRegister(managedResource, objectName);
        else {
            DynamicMBean mbean = adaptMBeanIfPossible(managedResource);
            if (mbean == null)
                mbean = createMBean(managedResource);
            doRegister(mbean, objectName);
        }
    }

    /* MIGHT BECOME OVERRIDABLE */

    public MBeanServer getMBeanServer() {
        return mBeanServer;
    }

    protected ExportBehavior getExportBehavior() {
        return exportBehavior;
    }

    protected JmxNamingStrategy getNamingStrategy() {
        return namingStrategy;
    }

    protected void doUnregister(ObjectName objectName) {
        try {
            getMBeanServer().unregisterMBean(objectName);
        } catch (JMException ignored) {
        }
    }

    protected void doRegister(Object managedResource, ObjectName objectName) {
        try {
            getMBeanServer().registerMBean(managedResource, objectName);
        } catch (InstanceAlreadyExistsException e) {
            if (getExportBehavior() == ExportBehavior.REPLACE_EXISTING) {
                doUnregister(objectName);
                try {
                    getMBeanServer().registerMBean(managedResource, objectName);
                } catch (JMException e2) {
                    throw new JmxExportException("Unable to register MBean [" + managedResource + "] with object name [" + objectName + "]", e2);
                }
            } else if (getExportBehavior() == ExportBehavior.FAIL_ON_EXISTING)
                throw new JmxExportException("Unable to register MBean [" + managedResource + "] with object name [" + objectName + "]", e);
        } catch (JMException e) {
            throw new JmxExportException("Unable to register MBean [" + managedResource + "] with object name [" + objectName + "]", e);
        }
    }

    /**
     * Build an adapted MBean for the given bean instance, if possible.
     * <p>The default implementation builds a JMX 1.2 StandardMBean
     * for the target's MBean/MXBean interface in case of an AOP proxy,
     * delegating the interface's management operations to the proxy.
     *
     * @param bean the original bean instance
     * @return the adapted MBean, or <code>null</code> if not possible
     */
    @SuppressWarnings("unchecked")
    protected DynamicMBean adaptMBeanIfPossible(Object bean) {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        if (targetClass != bean.getClass()) {
            Class ifc = JmxUtils.getMXBeanInterface(targetClass);
            if (ifc != null) {
                if (ifc.isInstance(bean))
                    return new StandardMBean(bean, ifc, true);
                throw new JmxExportException("Managed bean [" + bean + "] has a target class with an MXBean interface but does not expose it in the proxy");
            } else {
                ifc = JmxUtils.getMBeanInterface(targetClass);
                if (ifc != null) {
                    if (ifc.isInstance(bean))
                        return new StandardMBean(bean, ifc, false);
                    throw new JmxExportException("Managed bean [" + bean + "] has a target class with an MBean interface but does not expose it in the proxy");
                }
            }
        }
        return null;
    }

    protected DynamicMBean createMBean(Object managedResource) {
        Class<?> targetClass = AopUtils.getTargetClass(managedResource);
        JmxMetadata metadata = getJmxMetadata(targetClass);
        return new ContextualDynamicMBean(
                new DefaultDynamicMBean(managedResource, metadata),
                managedResource.getClass().getClassLoader());
    }

    protected JmxMetadata getJmxMetadata(Class<?> clazz) {
        return metadataAssembler.getMetadata(clazz);
    }
}
