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

import com.mycila.event.annotation.Publish;
import com.mycila.event.annotation.Reference;
import com.mycila.event.annotation.Subscribe;
import com.mycila.event.annotation.Veto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static com.mycila.event.Reachability.*;
import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class AnnotationProcessorTest {

    private final List<Object> sequence = new ArrayList<Object>();

    AnnotationProcessor processor;
    Dispatcher dispatcher;

    @Before
    public void setup() {
        dispatcher = Dispatchers.synchronousUnsafe(ErrorHandlers.rethrowErrorsAfterPublish());
        processor = AnnotationProcessor.create(dispatcher);
        sequence.clear();
    }

    @Test
    public void test_subscribe_strong() {
        Object o = new Object() {
            @Subscribe(topics = {"prog/events/a", "prog/events/b/**"}, eventType = String.class)
            private void handle(Event<String> event) {
                sequence.add(event.source());
            }
        };
        processor.process(o).registerSubscribers();
        publish();
        assertEquals(sequence.toString(), "[Hello for a, hello for b1, hello for b2]");
    }

    @Test
    public void test_subscribe_weak() {
        Object o = new Object() {
            @Subscribe(topics = {"prog/events/a", "prog/events/b/**"}, eventType = String.class)
            @Reference(WEAK)
            private void handle(Event<String> event) {
                sequence.add(event.source());
            }
        };
        processor.process(o).registerSubscribers();

        System.gc();
        System.gc();
        System.gc();

        publish();
        assertEquals(sequence.toString(), "[]");
    }

    @Test
    public void test_veto() {
        Object o = new Object() {
            @Subscribe(topics = {"prog/events/a", "prog/events/b/**"}, eventType = String.class)
            private void handle(Event<String> event) {
                sequence.add(event.source());
            }

            @Veto(topics = {"prog/events/b/**"}, eventType = String.class)
            void check(VetoableEvent<String> vetoable) {
                if (vetoable.event().source().contains("b1"))
                    vetoable.veto();
            }
        };

        processor.process(o)
                .registerSubscribers()
                .registerVetoers();

        publish();
        assertEquals(sequence.toString(), "[Hello for a, hello for b2]");

    }

    @Test
    public void test_multiple_publish() {
        Object o = new Object() {
            @Subscribe(topics = "prog/events/a/**", eventType = String.class)
            private void handle(Event<String> event) {
                sequence.add(event.source());
            }
        };
        processor.process(o).registerSubscribers();
        publish();
        assertEquals(sequence.toString(), "[Hello for a, Hello for a1, Hello for a1]");
    }

    private void publish() {
        class A {
            Publisher<Object> publisher;

            @Publish(topics = "prog/events/b/b1")
            private void send(Publisher<Object> publisher) {
                this.publisher = publisher;
            }

            void sendAll() {
                publisher.publish("hello for b1");
                publisher.publish(2);
                publisher.publish("hello for b2");
                publisher.publish(3);
            }
        }

        A a = new A();
        processor.process(a).injectPublishers();

        B b = processor.createPublisher(B.class);
        C c = processor.createPublisher(C.class);

        b.send("Hello for a", 1);

        a.sendAll();

        c.send("Hello for a1", 4);
    }

    private static interface B {
        @Publish(topics = "prog/events/a")
        void send(String a, int b);
    }

    static abstract class C {
        @Publish(topics = {"prog/events/a/a1", "prog/events/a/allA"})
        abstract void send(String a, int b);
    }
}
