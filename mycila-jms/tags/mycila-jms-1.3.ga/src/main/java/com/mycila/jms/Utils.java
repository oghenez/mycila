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

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Utils {

    private Utils() {
    }

    static Destination createDestination(Session session, String destination) {
        try {
            if (destination.startsWith("queue:"))
                return session.createQueue(destination.substring(6));
            if (destination.startsWith("topic:"))
                return session.createTopic(destination.substring(6));
        } catch (JMSException e) {
            Utils.close(session);
            throw new JMSClientException(e);
        }
        throw new IllegalArgumentException("Destination prefix missing. Please use: [queue|topic]:jms.destination.name");
    }

    public static MessageConsumer createConsumer(Session session, String destination, String selector) {
        try {
            Destination d = Utils.createDestination(session, destination);
            return session.createConsumer(d, selector, false);
        } catch (JMSException e) {
            Utils.close(session);
            throw new JMSClientException(e);
        }
    }

    public static MessageConsumer createConsumer(Session session, TemporaryQueue temporaryQueue, String selector) {
        try {
            return session.createConsumer(temporaryQueue, selector, false);
        } catch (JMSException e) {
            Utils.delete(temporaryQueue);
            Utils.close(session);
            throw new JMSClientException(e);
        }
    }

    public static MessageProducer createProducer(Session session, String destination, long ttl) {
        try {
            Destination d = Utils.createDestination(session, destination);
            MessageProducer producer = session.createProducer(d);
            producer.setDeliveryMode(destination.startsWith("topic:") ?
                    DeliveryMode.NON_PERSISTENT :
                    DeliveryMode.PERSISTENT);
            if (ttl >= 0)
                producer.setTimeToLive(ttl);
            return producer;
        } catch (JMSException e) {
            Utils.close(session);
            throw new JMSClientException(e);
        }
    }

    public static void close(Session session) {
        if (session != null)
            try {
                session.close();
            } catch (JMSException ignored) {
            }
    }

    public static void close(MessageConsumer consumer) {
        if (consumer != null)
            try {
                consumer.close();
            } catch (JMSException ignored) {
            }
    }

    public static void close(MessageProducer producer) {
        if (producer != null)
            try {
                producer.close();
            } catch (JMSException ignored) {
            }
    }

    static void delete(TemporaryQueue temporaryQueue) {
        if (temporaryQueue != null)
            try {
                temporaryQueue.delete();
            } catch (JMSException ignored) {
            }
    }

    public static TemporaryQueue createTemporaryQueue(Session session, Message jmsMessage) {
        TemporaryQueue temporaryQueue;
        try {
            temporaryQueue = session.createTemporaryQueue();
        } catch (JMSException e) {
            Utils.close(session);
            throw new JMSClientException(e);
        }
        try {
            jmsMessage.setJMSReplyTo(temporaryQueue);
        } catch (JMSException e) {
            Utils.delete(temporaryQueue);
            Utils.close(session);
            throw new JMSClientException(e);
        }
        return temporaryQueue;
    }

    public static Message receive(MessageConsumer consumer, long timeout, TimeUnit unit) throws TimeoutException, JMSException {
        long t = TimeUnit.MILLISECONDS.convert(timeout, unit);
        Message message = t >= 0 ? consumer.receive(t) : consumer.receive();
        if (message == null && t >= 0)
            throw new TimeoutException("Expected JMS message not received");
        if (message == null)
            throw new AssertionError("NULL response from JMS !");
        return message;
    }
}
