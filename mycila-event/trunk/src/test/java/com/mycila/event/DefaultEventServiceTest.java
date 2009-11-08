package com.mycila.event;

import com.mycila.event.impl.DefaultEventService;
import com.mycila.event.subscriber.StrongSubscriber;
import static com.mycila.event.topic.Topics.*;
import com.mycila.event.veto.StrongVetoer;
import com.mycila.event.veto.VetoableEvent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class DefaultEventServiceTest {

    private final List<Object> sequence = new ArrayList<Object>();

    EventService eventService;

    @Before
    public void setup() {
        eventService = new DefaultEventService();
        sequence.clear();
    }

    @Test
    public void test_subscribe_strong() {
        eventService.subscribe(only("prog/events/a").or(topics("prog/events/b/**")), String.class, new StrongSubscriber<String>() {
            @Override
            public void onEvent(String event) throws Exception {
                System.out.println("Got: " + event);
            }
        });
        publish();
        System.out.println(sequence);
    }

    @Test
    public void test_subscribe_weak() {
        eventService.subscribe(only("prog/events/a").or(topics("prog/events/b/**")), String.class, new StrongSubscriber<String>() {
            @Override
            public void onEvent(String event) throws Exception {
                System.out.println("Got: " + event);
            }
        });

        System.gc();
        System.gc();
        System.gc();

        publish();
        System.out.println(sequence);
    }

    @Test
    public void test_veto() {
        eventService.subscribe(only("prog/events/a").or(topics("prog/events/b/**")), String.class, new StrongSubscriber<String>() {
            @Override
            public void onEvent(String event) throws Exception {
                System.out.println("Got: " + event);
            }
        });
        eventService.register(topics("prog/events/b/**"), String.class, new StrongVetoer<String>() {
            @Override
            public void check(VetoableEvent<String> vetoable) {
                if(vetoable.event().contains("b1"))
                    vetoable.veto();
            }
        });

        publish();
        System.out.println(sequence);

    }

    private void publish() {
        eventService.publish(topic("prog/events/a"), "Hello for a");
        eventService.publish(topic("prog/events/a"), 1);

        eventService.publish(topic("prog/events/b/b1"), "hello for b1");
        eventService.publish(topic("prog/events/b/b1"), 2);

        eventService.publish(topic("prog/events/b/b1"), "hello for b2");
        eventService.publish(topic("prog/events/b/b1"), 3);

        eventService.publish(topic("prog/events/a/a1"), "hello for a1");
        eventService.publish(topic("prog/events/a/a1"), 4);
    }
}
