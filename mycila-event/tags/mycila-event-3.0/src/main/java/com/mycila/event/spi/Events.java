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

import com.mycila.event.api.Event;
import com.mycila.event.api.topic.Topic;

import static com.mycila.event.api.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Events {

    private Events() {
    }

    static <E> Event<E> event(final Topic topic, final E source) {
        notNull(topic, "Topic");
        notNull(source, "Source");
        return new Event<E>() {
            private final long timestamp = System.nanoTime();

            public Topic getTopic() {
                return topic;
            }

            public E getSource() {
                return source;
            }

            public long getTimestamp() {
                return timestamp;
            }

            @Override
            public String toString() {
                return "Event{timestamp=" + timestamp + ",topic=" + topic + ",source=" + source + "}";
            }
        };
    }

}
