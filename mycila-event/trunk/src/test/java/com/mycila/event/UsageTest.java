package com.mycila.event;

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
        Dispatcher dispatcher = Dispatchers.synchronous(true);

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
