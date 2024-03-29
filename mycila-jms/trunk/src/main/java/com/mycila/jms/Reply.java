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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Reply<T extends Serializable> extends EditableMessage<T> implements JMSReply<T> {

    final transient Sender sender;
    final String replyTo;

    Reply(InboundMessage<? extends Serializable> receivedMessage, T reply) {
        super(reply);
        this.sender = receivedMessage.sender;
        this.replyTo = receivedMessage.replyTo;
        this.headers.put(JMSHeader.CorrelationID, receivedMessage.headers.get(JMSHeader.CorrelationID));
    }

    @Override
    public void send() {
        sender.send(this, replyTo);
    }

    @Override
    public JMSInboundMessage<? extends Serializable> getResponse() throws TimeoutException {
        return sender.request(this, replyTo);
    }

    @Override
    public JMSInboundMessage<? extends Serializable> getResponse(long timeout, TimeUnit unit) throws TimeoutException {
        return sender.request(this, replyTo, timeout, unit);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "destination=" + replyTo +
                ", headers=" + headers +
                ", properties=" + properties +
                '}';
    }

}