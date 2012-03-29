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

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.io.Serializable;
import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */

final class MessageCreator {
    private final Session session;

    public MessageCreator(Session session) {
        this.session = session;
    }

    public Message createMessage(BasicMessage<? extends Serializable> message) {
        Message m = create(message);
        try {
            for (Map.Entry<JMSHeader, Serializable> entry : message.headers.entrySet()) {
                switch (entry.getKey()) {
                    case CorrelationID:
                        m.setJMSCorrelationID((String) entry.getValue());
                        break;
                    case Type:
                        m.setJMSType((String) entry.getValue());
                        break;
                    default:
                        throw new AssertionError("Unsupported attribute: " + entry.getKey());
                }
            }
            for (Map.Entry<String, Serializable> e : message.properties.entrySet()) {
                Object v = e.getValue();
                if (v instanceof Boolean) m.setBooleanProperty(e.getKey(), (Boolean) v);
                else if (v instanceof Byte) m.setByteProperty(e.getKey(), (Byte) v);
                else if (v instanceof Short) m.setShortProperty(e.getKey(), (Short) v);
                else if (v instanceof Integer) m.setIntProperty(e.getKey(), (Integer) v);
                else if (v instanceof Float) m.setFloatProperty(e.getKey(), (Float) v);
                else if (v instanceof Long) m.setLongProperty(e.getKey(), (Long) v);
                else if (v instanceof Double) m.setDoubleProperty(e.getKey(), (Double) v);
                else if (v instanceof String) m.setStringProperty(e.getKey(), (String) v);
                else m.setObjectProperty(e.getKey(), v instanceof Serializable ? v : String.valueOf(v));
            }
            return m;
        } catch (JMSException e) {
            Utils.close(session);
            throw new JMSClientException(e);
        }
    }

    private Message create(BasicMessage<? extends Serializable> message) {
        try {
            Serializable o = message.getBody();
            if (o instanceof String)
                return session.createTextMessage(String.valueOf(o));
            if (o instanceof Map) {
                MapMessage jmsMessage = session.createMapMessage();
                for (Map.Entry<Object, Object> entry : ((Map<Object, Object>) o).entrySet()) {
                    String k = String.valueOf(entry.getKey());
                    Object v = entry.getValue();
                    if (v instanceof Boolean) jmsMessage.setBoolean(k, (Boolean) v);
                    else if (v instanceof Byte) jmsMessage.setByte(k, (Byte) v);
                    else if (v instanceof Character) jmsMessage.setChar(k, (Character) v);
                    else if (v instanceof Short) jmsMessage.setShort(k, (Short) v);
                    else if (v instanceof Integer) jmsMessage.setInt(k, (Integer) v);
                    else if (v instanceof Float) jmsMessage.setFloat(k, (Float) v);
                    else if (v instanceof Long) jmsMessage.setLong(k, (Long) v);
                    else if (v instanceof Double) jmsMessage.setDouble(k, (Double) v);
                    else if (v instanceof String) jmsMessage.setString(k, (String) v);
                    else jmsMessage.setObject(k, v instanceof Serializable ? v : String.valueOf(v));
                }
                return jmsMessage;
            }
            if (o instanceof byte[]) {
                BytesMessage msg = session.createBytesMessage();
                msg.writeBytes((byte[]) o);
                return msg;
            }
            return session.createObjectMessage(o);
        } catch (JMSException e) {
            Utils.close(session);
            throw new JMSClientException(e);
        }
    }

}
