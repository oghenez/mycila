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

package com.mycila.event;

import com.mycila.event.api.ErrorHandlers;
import com.mycila.event.api.Event;
import com.mycila.event.api.Dispatcher;
import com.mycila.event.api.Subscriber;
import com.mycila.event.api.TopicMatcher;
import static com.mycila.event.api.Topics.*;
import com.mycila.event.api.VetoableEvent;
import com.mycila.event.api.Vetoer;
import com.mycila.event.impl.Dispatchers;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class UsageTest {
    public static void main(String... args) {
        // first create an event service
        Dispatcher dispatcher = Dispatchers.SYNCHRONOUS_DISPATCHER.create(ErrorHandlers.rethrowErrorsAfterPublish());

        // then subscribe
        TopicMatcher matcher = only("app/events/swing/button").or(topics("app/events/swing/fields/**"));
        dispatcher.subscribe(matcher, String.class, new Subscriber<String>() {
            @Override
            public void onEvent(Event<String> event) throws Exception {
                System.out.println("Received: " + event.source());
            }
        });

        // you can add a listener to oppose a veto to the events
        dispatcher.register(only("app/events/swing/button"), String.class, new Vetoer<String>() {
            @Override
            public void check(VetoableEvent<String> vetoableEvent) {
                if (vetoableEvent.event().source().equals("password"))
                    vetoableEvent.veto();
            }
        });

        // and publish
        dispatcher.publish(topic("app/events/swing/button"), "Hello !");
    }
}
