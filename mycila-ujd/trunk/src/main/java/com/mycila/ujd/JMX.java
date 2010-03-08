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
