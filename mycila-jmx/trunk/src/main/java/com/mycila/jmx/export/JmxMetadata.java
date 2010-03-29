package com.mycila.jmx.export;

import javax.management.AttributeNotFoundException;
import javax.management.MBeanInfo;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface JmxMetadata {
    MBeanInfo getMBeanInfo();

    JmxAttribute getAttribute(String attribute) throws AttributeNotFoundException;

    JmxOperation getOperation(String operation, Class<?>... paramTypes) throws OperationNotFoundException;
}
