package com.mycila.event;

import com.mycila.event.api.EventService;
import com.mycila.event.api.veto.HardVetoer;
import com.mycila.event.api.event.Event;
import com.mycila.event.api.event.VetoableEvent;
import com.mycila.event.api.subscriber.HardSubscriber;
import com.mycila.event.api.topic.TopicMatcher;
import static com.mycila.event.api.topic.Topics.*;
import com.mycila.event.impl.EventServices;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class UsageTest {
    public static void main(String... args) {
        // first create an event service
        EventService eventService = EventServices.newEventService();

        // then subscribe
        TopicMatcher matcher = only("app/events/swing/button").or(topics("app/events/swing/fields/**"));
        eventService.subscribe(matcher, String.class, new HardSubscriber<String>() {
            @Override
            public void onEvent(Event<String> event) throws Exception {
                System.out.println("Received: " + event.source());
            }
        });

        // you can add a listener to oppose a veto to the events
        eventService.register(only("app/events/swing/button"), String.class, new HardVetoer<String>() {
            @Override
            public void check(VetoableEvent<String> vetoableEvent) {
                if(vetoableEvent.event().source().equals("password"))
                    vetoableEvent.veto();
            }
        });

        // and publish
        eventService.publish(topic("app/events/swing/button"), "Hello !");
    }
}
