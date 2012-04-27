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

import javax.jms.ConnectionFactory;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import java.io.Serializable;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

import static com.mycila.jms.Query.q;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class SimpleClient implements Sender, JMSClient {

    private final IdentityHashMap<JMSListener, Session> listeners = new IdentityHashMap<JMSListener, Session>();
    private final SessionManager sessionManager;
    private final AtomicLong timeout = new AtomicLong(1000 * 60);
    private final AtomicLong ttl = new AtomicLong(1000 * 60 * 30);

    public SimpleClient(ConnectionFactory connectionFactory, ExceptionListener exceptionListener) {
        this(connectionFactory, UUID.randomUUID().toString(), exceptionListener);
    }

    public SimpleClient(ConnectionFactory connectionFactory, String clientId, ExceptionListener exceptionListener) {
        this.sessionManager = new SessionManager(connectionFactory, clientId, exceptionListener);
    }

    /**
     * Change maximum time to wait for a response. Default to 1 minute. Set it to -1 to disable.
     */
    public void setResponseTimeout(long timeout, TimeUnit unit) {
        this.timeout.set(TimeUnit.MILLISECONDS.convert(timeout, unit));
    }

    /**
     * Change the maximum time a message can remain on a queue if not delivered. Default to 30 minute. Set it to -1 to disable.
     */
    public void setMessageTTL(long ttl, TimeUnit unit) {
        this.ttl.set(TimeUnit.MILLISECONDS.convert(ttl, unit));
    }

    @Override
    public JMSMetaData getMetaData() {
        return sessionManager.getJMSMetaData();
    }

    @Override
    public boolean isStarted() {
        return sessionManager.isStarted();
    }

    @Override
    public String getClientId() {
        return sessionManager.getClientId();
    }

    @Override
    public void start() {
        sessionManager.start();
    }

    @Override
    public void stop() {
        sessionManager.stop();
        for (Session session : listeners.values())
            Utils.close(session);
    }

    @Override
    public <T extends Serializable> JMSOutboundMessage<T> createMessage(T message) {
        return new OutboundMessage<T>(this, message);
    }

    @Override
    public <T extends Serializable> JMSOutboundMessage<T> createMessage(T message, Map<String, Serializable> properties) {
        JMSOutboundMessage<T> m = createMessage(message);
        m.setProperties(properties);
        return m;
    }

    @Override
    public void subscribe(final String destination, final JMSListener listener) {
        subscribe(destination, null, listener);
    }

    @Override
    public void subscribe(final String destination, final String selector, final JMSListener listener) {
        Session session = sessionManager.createSession();
        try {
            MessageConsumer consumer = Utils.createConsumer(session, destination, selector);
            listeners.put(listener, session);
            consumer.setMessageListener(new MessageListener() {
                @Override
                public void onMessage(Message message) {
                    listener.onMessage(new InboundMessage<Serializable>(SimpleClient.this, message));
                }
            });
        } catch (JMSException e) {
            throw new JMSClientException(e);
        }
    }

    @Override
    public void unsubscribe(JMSListener listener) {
        Session session = listeners.remove(listener);
        if (session != null)
            try {
                session.close();
            } catch (JMSException ignored) {
            }
    }

    @Override
    public <T extends Serializable> JMSInboundMessage<T> receive(final String destination) throws TimeoutException {
        return receive(destination, null, timeout.get(), TimeUnit.MILLISECONDS);
    }

    @Override
    public <T extends Serializable> JMSInboundMessage<T> receive(final String destination, final String selector) throws TimeoutException {
        return receive(destination, selector, timeout.get(), TimeUnit.MILLISECONDS);
    }

    @Override
    public <T extends Serializable> JMSInboundMessage<T> receive(final String destination, final long timeout, final TimeUnit unit) throws TimeoutException {
        return receive(destination, null, timeout, unit);
    }

    @Override
    public <T extends Serializable> JMSInboundMessage<T> receive(final String destination, final String selector, final long timeout, final TimeUnit unit) throws TimeoutException {
        return new InboundMessage<T>(this, sessionManager.execute(new WithinTimedSession<Message>() {
            @Override
            public Message execute(Session session) throws JMSException, TimeoutException {
                MessageConsumer consumer = Utils.createConsumer(session, destination, selector);
                try {
                    return Utils.receive(consumer, timeout, unit);
                } finally {
                    Utils.close(consumer);
                }
            }
        }));
    }

    @Override
    public void send(final EditableMessage<? extends Serializable> message, final String destination) {
        final Session session = sessionManager.createSession();
        final MessageProducer producer = Utils.createProducer(session, destination, ttl.get());
        final Message jmsMessage = new MessageCreator(session).createMessage(message);
        if (message.getListener() != null) {
            TemporaryQueue temporaryQueue = Utils.createTemporaryQueue(session, jmsMessage);
            Listener.create(this, session, message, temporaryQueue);
        }
        try {
            jmsMessage.setStringProperty(JMSXProperty.FromClientID, sessionManager.getClientId());
            producer.send(jmsMessage);
        } catch (JMSException e) {
            Utils.close(session);
            throw new JMSClientException(e);
        } finally {
            Utils.close(producer);
            if (message.getListener() == null)
                Utils.close(session);
        }
    }

    @Override
    public InboundMessage<? extends Serializable> request(EditableMessage<? extends Serializable> message, String destination) throws TimeoutException {
        return request(message, destination, timeout.get(), TimeUnit.MILLISECONDS);
    }

    @Override
    public InboundMessage<? extends Serializable> request(EditableMessage<? extends Serializable> message, String destination, long timeout, TimeUnit unit) throws TimeoutException {
        Session session = sessionManager.createSession();
        MessageProducer producer = Utils.createProducer(session, destination, ttl.get());
        Message jmsMessage = new MessageCreator(session).createMessage(message);
        TemporaryQueue temporaryQueue = Utils.createTemporaryQueue(session, jmsMessage);
        try {
            jmsMessage.setStringProperty(JMSXProperty.FromClientID, sessionManager.getClientId());
            producer.send(jmsMessage);
        } catch (JMSException e) {
            Utils.close(session);
            throw new JMSClientException(e);
        } finally {
            Utils.close(producer);
        }
        MessageConsumer consumer = Utils.createConsumer(session, temporaryQueue, q().header(JMSHeader.CorrelationID).eq(message.getConversationId()).build());
        try {
            jmsMessage = Utils.receive(consumer, timeout, unit);
        } catch (JMSException e) {
            throw new JMSClientException(e);
        } finally {
            Utils.close(consumer);
            Utils.close(session);
        }
        InboundMessage<Serializable> response = new InboundMessage<Serializable>(SimpleClient.this, jmsMessage);
        if (message.getListener() != null)
            message.getListener().onMessage(response);
        return response;
    }

}
