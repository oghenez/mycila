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

package tmp.spring;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.Map;

/**
 * Internal helper class for managing a JMX connector.
 *
 * @author Juergen Hoeller
 * @since 2.5.2
 */
class ConnectorDelegate {

    private JMXConnector connector;


    /**
     * Connects to the remote <code>MBeanServer</code> using the configured <code>JMXServiceURL</code>:
     * to the specified JMX service, or to a local MBeanServer if no service URL specified.
     *
     * @param serviceUrl  the JMX service URL to connect to (may be <code>null</code>)
     * @param environment the JMX environment for the connector (may be <code>null</code>)
     * @param agentId     the local JMX MBeanServer's agent id (may be <code>null</code>)
     */
    public MBeanServerConnection connect(JMXServiceURL serviceUrl, Map<String, ?> environment, String agentId)
            throws MBeanServerNotFoundException {

        if (serviceUrl != null) {

            try {
                this.connector = JMXConnectorFactory.connect(serviceUrl, environment);
                return this.connector.getMBeanServerConnection();
            }
            catch (IOException ex) {
                throw new MBeanServerNotFoundException("Could not connect to remote MBeanServer [" + serviceUrl + "]", ex);
            }
        } else {

            return JmxUtils.locateMBeanServer(agentId);
        }
    }

    /**
     * Closes any <code>JMXConnector</code> that may be managed by this interceptor.
     */
    public void close() {
        if (this.connector != null) {
            try {
                this.connector.close();
            }
            catch (IOException ex) {

            }
        }
    }

}