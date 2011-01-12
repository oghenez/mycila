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
import java.util.EnumMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
abstract class BasicMessage<T extends Serializable> implements JMSMessage<T>, Serializable {

    final T message;
    final Map<JMSHeader, Serializable> headers = new EnumMap<JMSHeader, Serializable>(JMSHeader.class);
    final Map<String, Serializable> properties = new TreeMap<String, Serializable>();

    BasicMessage(T message) {
        this.message = message;
        headers.put(JMSHeader.CorrelationID, UUID.randomUUID().toString());
    }

    @Override
    public final String getConversationId() {
        return (String) headers.get(JMSHeader.CorrelationID);
    }

    @Override
    public final T getBody() {
        return message;
    }

    @Override
    public final Map<String, Serializable> getProperties() {
        return properties;
    }

    @Override
    public final Serializable getProperty(String name) {
        return properties.get(name);
    }

    @Override
    public String getPropertyAsString(String name) {
        Object val = getProperty(name);
        return val == null ? null : val.toString();
    }

    @Override
    public Boolean getPropertyAsBoolean(String name) {
        Object val = getProperty(name);
        return val == null ? null : Boolean.valueOf(val.toString());
    }

    @Override
    public Long getPropertyAsLong(String name) {
        try {
            return Long.valueOf(getProperty(name).toString());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Byte getPropertyAsByte(String name) {
        try {
            return Byte.valueOf(getProperty(name).toString());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Integer getPropertyAsInt(String name) {
        try {
            return Integer.valueOf(getProperty(name).toString());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Short getPropertyAsShort(String name) {
        try {
            return Short.valueOf(getProperty(name).toString());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Float getPropertyAsFloat(String name) {
        try {
            return Float.valueOf(getProperty(name).toString());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Double getPropertyAsDouble(String name) {
        try {
            return Double.valueOf(getProperty(name).toString());
        } catch (Exception e) {
            return null;
        }
    }

}