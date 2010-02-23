package com.mycila.jmx.export;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

public interface SelfNaming {
    ObjectName getObjectName() throws MalformedObjectNameException;
}
