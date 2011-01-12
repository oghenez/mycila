/**
 * Copyright (C) 2010 Mycila <mathieu.carbou@gmail.com>
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

package com.mycila.jms.tool;

import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.TransportConnector;

import java.net.URI;
import java.util.concurrent.CountDownLatch;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class LightBroker {

    private final BrokerService broker;

    /**
     * Create a light JMS broker
     *
     * @param brokerName   The broker name
     * @param discoveryURI The rendez-vous address to use, in example: rendezvous://lightbrokers
     */
    public LightBroker(String brokerName, String discoveryURI) {
        try {
            final TransportConnector transportConnector = new TransportConnector();
            transportConnector.setUri(new URI("tcp://0.0.0.0:0"));
            transportConnector.setDiscoveryUri(new URI(discoveryURI));
            broker = new BrokerService();
            broker.setPersistent(false);
            broker.setUseJmx(false);
            broker.getManagementContext().setFindTigerMbeanServer(true);
            broker.setEnableStatistics(false);
            broker.setSupportFailOver(true);
            broker.setUseShutdownHook(true);
            broker.setBrokerName(brokerName);
            broker.addConnector(transportConnector);
            broker.addNetworkConnector(discoveryURI);
            broker.setPopulateJMSXUserID(true);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public BrokerService getBroker() {
        return broker;
    }

    public void start() {
        if (!broker.isStarted()) {
            final CountDownLatch started = new CountDownLatch(1);
            new Thread("broker-thread") {
                @Override
                public void run() {
                    try {
                        broker.start();
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                    while (!broker.isStarted()) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException ignored) {
                            break;
                        }
                    }
                    started.countDown();
                }
            }.start();
            try {
                started.await();
            } catch (InterruptedException ignored) {
            }
        }
    }

    public void stop() {
        if (broker.isStarted()) {
            try {
                broker.stop();
            } catch (Exception ignored) {
            }
        }
    }

}