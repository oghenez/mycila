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

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import java.io.Serializable;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Listener implements MessageListener {

    final TemporaryQueue temporaryQueue;
    final MessageConsumer consumer;
    final Session session;
    final EditableMessage<? extends Serializable> message;
    final SimpleClient client;

    Listener(SimpleClient client, Session session, EditableMessage<? extends Serializable> message, TemporaryQueue temporaryQueue) {
        this.client = client;
        this.temporaryQueue = temporaryQueue;
        this.session = session;
        this.message = message;
        this.consumer = Utils.createConsumer(session, temporaryQueue, Query.q().header(JMSHeader.CorrelationID).eq(message.getConversationId()).build());
        try {
            this.consumer.setMessageListener(this);
        } catch (JMSException e) {
            Utils.delete(temporaryQueue);
            Utils.close(consumer);
            Utils.close(session);
            throw new JMSClientException(e);
        }
    }

    @Override
    public void onMessage(Message message) {
        Utils.delete(temporaryQueue);
        Utils.close(consumer);
        Utils.close(session);
        JMSListener listener = this.message.listener;
        if (listener != null)
            listener.onMessage(new InboundMessage<Serializable>(client, message));
    }

    public static Listener create(SimpleClient client, Session session, EditableMessage<? extends Serializable> message, TemporaryQueue temporaryQueue) {
        return new Listener(client, session, message, temporaryQueue);
    }

}
