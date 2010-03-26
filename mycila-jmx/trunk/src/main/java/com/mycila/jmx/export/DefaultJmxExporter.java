package com.mycila.jmx.export;

import com.mycila.jmx.JmxUtils;

import javax.management.InstanceAlreadyExistsException;
import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class DefaultJmxExporter implements JmxExporter {

    private final MBeanServer mBeanServer;
    private final ExportBehavior exportBehavior = ExportBehavior.FAIL_ON_EXISTING;
    private final JmxNamingStrategy namingStrategy = new DefaultJmxNamingStrategy();

    public DefaultJmxExporter(MBeanServer mBeanServer) {
        this.mBeanServer = mBeanServer;
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
            Object mbean = createMBean(managedResource);
            doRegister(mbean, objectName);
        }
    }

    /* NON OVERRIDABLE */

    public final MBeanServer getMBeanServer() {
        return mBeanServer;
    }

    /* OVERRIDABLE */

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

    protected Object createMBean(Object managedResource) {
        return null;
    }

}
