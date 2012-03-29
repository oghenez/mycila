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
final class OutboundMessage<T extends Serializable> extends EditableMessage<T> implements JMSOutboundMessage<T> {

    private transient final Sender sender;

    public OutboundMessage(Sender sender, T message) {
        super(message);
        this.sender = sender;
    }

    @Override
    public void send(final String destination) {
        sender.send(this, destination);
    }

    @Override
    public JMSInboundMessage<? extends Serializable> getResponse(final String destination) throws TimeoutException {
        return sender.request(this, destination);
    }

    @Override
    public JMSInboundMessage<? extends Serializable> getResponse(String destination, long timeout, TimeUnit unit) throws TimeoutException {
        return sender.request(this, destination, timeout, unit);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "attributes=" + headers +
                ", properties=" + properties +
                '}';
    }
}
