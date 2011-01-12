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

package com.mycila.jms.test;


import com.mycila.jms.JMSInboundMessage
import com.mycila.jms.JMSListener
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class MockedListener implements JMSListener {
    def response
    JMSInboundMessage<? extends Serializable> message
    def latch = new CountDownLatch(1)

    def reset(Map map = new HashMap()) {
        latch = new CountDownLatch(1)
        response = map?.response
    }

    @Override
    void onMessage(JMSInboundMessage<? extends Serializable> message) {
        this.message = message
        latch.countDown()
        if (response)
            message.createReply(response).send();
    }

    JMSInboundMessage<? extends Serializable> getMessage() {
        if (latch.await(5, TimeUnit.SECONDS))
            return message;
        throw new TimeoutException("Expected message not received");
    }
}
