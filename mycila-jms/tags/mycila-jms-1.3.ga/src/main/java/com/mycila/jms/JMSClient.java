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

package com.mycila.jms;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface JMSClient {
    String getClientId();

    JMSMetaData getMetaData();

    boolean isStarted();

    void start();

    void stop();

    <T extends Serializable> JMSOutboundMessage<T> createMessage(T message);

    <T extends Serializable> JMSOutboundMessage<T> createMessage(T message, Map<String, Serializable> properties);

    void subscribe(String destination, JMSListener listener);

    void subscribe(String destination, String selector, JMSListener listener);

    void unsubscribe(JMSListener listener);

    <T extends Serializable> JMSInboundMessage<T> receive(String destination) throws TimeoutException;

    <T extends Serializable> JMSInboundMessage<T> receive(String destination, String selector) throws TimeoutException;

    <T extends Serializable> JMSInboundMessage<T> receive(String destination, long timeout, TimeUnit unit) throws TimeoutException;

    <T extends Serializable> JMSInboundMessage<T> receive(String destination, String selector, long timeout, TimeUnit unit) throws TimeoutException;
}
