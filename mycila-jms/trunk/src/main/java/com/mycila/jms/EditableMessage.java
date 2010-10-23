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

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
abstract class EditableMessage<T extends Serializable> extends BasicMessage<T> implements JMSEditableMessage<T> {

    transient volatile JMSListener listener;

    EditableMessage(T message) {
        super(message);
    }

    @Override
    public void setResponseListener(JMSListener listener) {
        this.listener = listener;
    }

    @Override
    public final void setProperties(Map<String, Serializable> props) {
        for (Map.Entry<String, Serializable> entry : props.entrySet())
            setProperty(entry.getKey(), entry.getValue());
    }

    @Override
    public final void setProperty(String name, Serializable value) {
        if (value == null)
            properties.remove(name);
        else
            properties.put(name, value);
    }

    public JMSListener getListener() {
        return listener;
    }
}