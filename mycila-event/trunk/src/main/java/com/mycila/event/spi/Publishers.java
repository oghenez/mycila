/**
 * Copyright (C) 2009 Mathieu Carbou <mathieu.carbou@gmail.com>
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

package com.mycila.event.spi;

import com.mycila.event.api.Dispatcher;
import com.mycila.event.api.message.MessageRequest;
import com.mycila.event.api.message.Messages;
import com.mycila.event.api.topic.Topic;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Publishers {

    private Publishers() {
    }

    static Publisher<Object> createPublisher(final Dispatcher dispatcher, final Topic... topics) {
        return new Publisher<Object>() {
            @Override
            public Topic[] getTopics() {
                return topics;
            }

            @Override
            public void publish(Object... events) {
                for (Object event : events)
                    for (Topic topic : topics)
                        dispatcher.publish(topic, event);
            }

            @Override
            public String toString() {
                return "Publisher on " + Arrays.deepToString(topics);
            }
        };
    }

    static Requestor<Object, Object> createRequestor(final Dispatcher dispatcher, final Topic topic, final long timeout, final TimeUnit unit) {
        return new Requestor<Object, Object>() {
            @Override
            public Topic getTopic() {
                return topic;
            }

            @Override
            public Object request(Object parameter) throws InterruptedException, TimeoutException {
                MessageRequest<Object> req = Messages.createRequest(parameter);
                dispatcher.publish(topic, req);
                return timeout < 0 ?
                        req.getResponse() :
                        req.getResponse(timeout, unit);
            }

            @Override
            public String toString() {
                return "Requestor on " + topic;
            }
        };
    }

}
