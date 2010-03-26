package com.mycila.jmx.export;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * Interface that allows infrastructure components to provide their own
 * <code>ObjectName</code>s to the <code>MBeanExporter</code>.
 *
 * @author Rob Harrop
 * @since 1.2.2
 */
public interface JmxSelfNaming {
    /**
     * @return the <code>ObjectName</code> for the implementing object.
     * @throws javax.management.MalformedObjectNameException
     *          if thrown by the ObjectName constructor
     */
    ObjectName getObjectName() throws MalformedObjectNameException;
}