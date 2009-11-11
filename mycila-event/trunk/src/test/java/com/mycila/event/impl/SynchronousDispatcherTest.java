package com.mycila.event.impl;

import com.mycila.event.api.*;

import static com.mycila.event.api.Topics.*;

import com.mycila.event.api.annotation.Reference;
import com.mycila.event.api.Reachability;
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
public final class SynchronousDispatcherTest {

    private final List<Object> sequence = new ArrayList<Object>();

    private Dispatcher dispatcher;

    @Before
    public void setup() {
        dispatcher = Dispatchers.synchronous(true);
        sequence.clear();
    }

    @Test
    public void test_subscribe_strong() {
        dispatcher.subscribe(only("prog/events/a").or(topics("prog/events/b/**")), String.class, new Subscriber<String>() {
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
        dispatcher.subscribe(only("prog/events/a").or(topics("prog/events/b/**")), String.class, new C());

        System.gc();
        System.gc();
        System.gc();

        publish();
        assertEquals(sequence.toString(), "[]");
    }

    @Test
    public void test_veto() {
        dispatcher.subscribe(only("prog/events/a").or(topics("prog/events/b/**")), String.class, new Subscriber<String>() {
            @Override
            public void onEvent(Event<String> event) throws Exception {
                sequence.add(event.source());
            }
        });
        dispatcher.register(topics("prog/events/b/**"), String.class, new Vetoer<String>() {
            @Override
            public void check(VetoableEvent<String> vetoable) {
                if (vetoable.event().source().contains("b1"))
                    vetoable.veto();
            }
        });

        publish();
        assertEquals(sequence.toString(), "[Hello for a, hello for b2]");

    }

    //TODO: test multithreading for blocking and non blocking

    private void publish() {
        dispatcher.publish(topic("prog/events/a"), "Hello for a");
        dispatcher.publish(topic("prog/events/a"), 1);

        dispatcher.publish(topic("prog/events/b/b1"), "hello for b1");
        dispatcher.publish(topic("prog/events/b/b1"), 2);

        dispatcher.publish(topic("prog/events/b/b1"), "hello for b2");
        dispatcher.publish(topic("prog/events/b/b1"), 3);

        dispatcher.publish(topic("prog/events/a/a1"), "hello for a1");
        dispatcher.publish(topic("prog/events/a/a1"), 4);
    }
}
