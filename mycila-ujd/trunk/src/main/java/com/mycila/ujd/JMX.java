package com.mycila.ujd;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class JMX {

    private JMX() {
    }


    static void register(String objectName, Object o) throws Exception {
        register(new ObjectName(objectName), o);
    }

    static void register(ObjectName objectName, Object o) throws Exception {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        try {
            if (server.isRegistered(objectName))
                server.unregisterMBean(objectName);
        } catch (Exception ignored) {
        }
        server.registerMBean(o, objectName);
    }


}
