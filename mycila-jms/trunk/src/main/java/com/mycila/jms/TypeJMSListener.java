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
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class TypeJMSListener<T> implements JMSListener {

    private final transient Logger logger = Logger.getLogger(getClass().getName());
    private final Class<T> expectedType;

    protected TypeJMSListener(Class<T> expectedType) {
        this.expectedType = expectedType;
    }

    @Override
    public final void onMessage(JMSInboundMessage<? extends Serializable> message) {
        Object body = message.getBody();
        if (!expectedType.isInstance(body)) {
            logger.log(Level.SEVERE, "Expecting body type " + expectedType.getName() + " but received " + body);
        } else {
            try {
                handle(expectedType.cast(body));
            } catch (RuntimeException e) {
                logger.log(Level.SEVERE, "Failure in JMS subscriber for body: " + body + " : " + e.getMessage(), e);
                throw e;
            }
        }
    }

    protected abstract void handle(T body);
}
