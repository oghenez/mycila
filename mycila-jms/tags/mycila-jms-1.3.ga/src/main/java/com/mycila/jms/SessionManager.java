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

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Session;
import java.util.concurrent.TimeoutException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class SessionManager {

    final String clientId;
    final ConnectionFactory connectionFactory;
    final ExceptionListener exceptionListener;
    private volatile Connection connection;
    private volatile JMSMetaData metaData;

    public SessionManager(ConnectionFactory connectionFactory, String clientId, ExceptionListener exceptionListener) {
        this.connectionFactory = connectionFactory;
        this.clientId = clientId;
        this.exceptionListener = exceptionListener;
    }

    public String getClientId() {
        return clientId;
    }

    public boolean isStarted() {
        return connection != null;
    }

    public synchronized void start() {
        if (!isStarted()) {
            try {
                connection = connectionFactory.createConnection();
                connection.setClientID(this.clientId);
                connection.setExceptionListener(exceptionListener);
                connection.start();
                metaData = new JMSMetaDataImpl(connection.getMetaData());
            } catch (JMSException e) {
                throw new JMSClientException(e);
            }
        } else
            throw new JMSClientException("Already connected !");
    }

    public <T> T execute(WithinTimedSession<T> withinSession) throws TimeoutException {
        Session session = createSession();
        try {
            return withinSession.execute(session);
        } catch (JMSException e) {
            throw new JMSClientException(e);
        } finally {
            try {
                session.close();
            } catch (JMSException ignored) {
            }
        }
    }

    public Session createSession() {
        if (!isStarted())
            throw new IllegalStateException("JMSClient not started !");
        try {
            return connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            throw new JMSClientException(e);
        }
    }

    public JMSMetaData getJMSMetaData() {
        if (!isStarted())
            throw new IllegalStateException("JMSClient not started !");
        return metaData;
    }

    public void stop() {
        try {
            connection.close();
        } catch (Exception ignored) {
        } finally {
            connection = null;
            metaData = null;
        }
    }
}
