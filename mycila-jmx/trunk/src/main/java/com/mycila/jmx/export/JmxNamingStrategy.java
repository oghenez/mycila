package com.mycila.jmx.export;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface JmxNamingStrategy {
    /**
     * Obtain an <code>ObjectName</code> for the supplied bean.
     *
     * @param managedBean the bean that will be exposed under the
     *                    returned <code>ObjectName</code>
     * @return the <code>ObjectName</code> instance
     * @throws javax.management.MalformedObjectNameException
     *          if the resulting <code>ObjectName</code> is invalid
     */
    ObjectName getObjectName(Object managedBean) throws MalformedObjectNameException;
}
