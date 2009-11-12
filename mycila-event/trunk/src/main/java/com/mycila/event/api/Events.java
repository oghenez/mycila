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

package com.mycila.event.api;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.mycila.event.api.util.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Events {

    private Events() {
    }

    public static <E> Event<E> event(final Topic topic, final E source) {
        notNull(topic, "Topic");
        notNull(source, "Source");
        return new Event<E>() {
            private final long timestamp = System.nanoTime();

            @Override
            public Topic topic() {
                return topic;
            }

            @Override
            public E source() {
                return source;
            }

            @Override
            public long timestamp() {
                return timestamp;
            }

            @Override
            public String toString() {
                return "Event{timestamp=" + timestamp + ",topic=" + topic + ",type=" + source.getClass().getName() + "}";
            }
        };
    }

    public static <E> VetoableEvent<E> vetoable(final Topic topic, final E source) {
        return vetoable(event(topic, source));
    }

    public static <E> VetoableEvent<E> vetoable(final Event<E> event) {
        notNull(event, "Event");
        return new VetoableEvent<E>() {
            private final AtomicBoolean allowed = new AtomicBoolean(true);

            @Override
            public Event<E> event() {
                return event;
            }

            @Override
            public void veto() {
                allowed.set(false);
            }

            @Override
            public boolean isAllowed() {
                return allowed.get();
            }

            @Override
            public String toString() {
                return "VetoableEvent{timestamp=" + event.timestamp() + ",topic=" + event.topic() + ",type=" + event.source().getClass().getName() + ",allowed=" + allowed + "}";
            }
        };
    }

}
