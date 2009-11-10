package com.mycila.event.impl;

import com.mycila.event.api.EventService;
import com.mycila.event.api.event.Event;
import com.mycila.event.api.event.VetoableEvent;
import com.mycila.event.api.subscriber.HardSubscriber;
import com.mycila.event.api.subscriber.WeakSubscriber;
import static com.mycila.event.api.topic.Topics.*;
import com.mycila.event.api.veto.HardVetoer;
import static org.junit.Assert.*;
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

    private EventService eventService;

    @Before
    public void setup() {
        eventService = new DefaultEventService();
        sequence.clear();
    }

    @Test
    public void test_subscribe_strong() {
        eventService.subscribe(only("prog/events/a").or(topics("prog/events/b/**")), String.class, new HardSubscriber<String>() {
            @Override
            public void onEvent(Event<String> event) throws Exception {
                sequence.add(event.source());
            }
        });
        publish();
        assertEquals(sequence.toString(), "[Hello for a, hello for b1, hello for b2]");
    }

    @Test
    public void test_subscribe_weak() {
        eventService.subscribe(only("prog/events/a").or(topics("prog/events/b/**")), String.class, new WeakSubscriber<String>() {
            @Override
            public void onEvent(Event<String> event) throws Exception {
                sequence.add(event.source());
            }
        });

        System.gc();
        System.gc();
        System.gc();

        publish();
        assertEquals(sequence.toString(), "[]");
    }

    @Test
    public void test_veto() {
        eventService.subscribe(only("prog/events/a").or(topics("prog/events/b/**")), String.class, new HardSubscriber<String>() {
            @Override
            public void onEvent(Event<String> event) throws Exception {
                sequence.add(event.source());
            }
        });
        eventService.register(topics("prog/events/b/**"), String.class, new HardVetoer<String>() {
            @Override
            public void check(VetoableEvent<String> vetoable) {
                if(vetoable.event().source().contains("b1"))
                    vetoable.veto();
            }
        });

        publish();
        assertEquals(sequence.toString(), "[Hello for a, hello for b2]");

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
