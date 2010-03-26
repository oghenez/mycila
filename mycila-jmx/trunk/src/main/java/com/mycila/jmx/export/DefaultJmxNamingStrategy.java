package com.mycila.jmx.export;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class DefaultJmxNamingStrategy implements JmxNamingStrategy {
    @Override
    public ObjectName getObjectName(Object managedBean) throws MalformedObjectNameException {
        return null;
    }
}
