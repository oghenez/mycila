package com.mycila.event.impl;

import com.mycila.event.api.*;

import static com.mycila.event.api.Topics.*;

import com.mycila.event.api.annotation.Reference;
import com.mycila.event.api.ref.Reachability;
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
        eventService = EventServices.newEventService();
        sequence.clear();
    }

    @Test
    public void test_subscribe_strong() {
        eventService.subscribe(only("prog/events/a").or(topics("prog/events/b/**")), String.class, new Subscriber<String>() {
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
        @Reference(Reachability.WEAK)
        class C implements Subscriber<String> {
            @Override
            public void onEvent(Event<String> event) throws Exception {
                sequence.add(event.source());
            }
        }
        eventService.subscribe(only("prog/events/a").or(topics("prog/events/b/**")), String.class, new C());

        System.gc();
        System.gc();
        System.gc();

        publish();
        assertEquals(sequence.toString(), "[]");
    }

    @Test
    public void test_veto() {
        eventService.subscribe(only("prog/events/a").or(topics("prog/events/b/**")), String.class, new Subscriber<String>() {
            @Override
            public void onEvent(Event<String> event) throws Exception {
                sequence.add(event.source());
            }
        });
        eventService.register(topics("prog/events/b/**"), String.class, new Vetoer<String>() {
            @Override
            public void check(VetoableEvent<String> vetoable) {
                if (vetoable.event().source().contains("b1"))
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
