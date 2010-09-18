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

import com.mycila.event.Dispatcher;
import com.mycila.event.Event;
import com.mycila.event.annotation.Publish;
import com.mycila.event.annotation.Reference;
import com.mycila.event.annotation.Subscribe;
import com.mycila.event.internal.Dispatchers;
import com.mycila.event.internal.ErrorHandlers;
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
        dispatcher = Dispatchers.synchronousUnsafe(ErrorHandlers.rethrow());
        processor = AnnotationProcessors.create(dispatcher);
        sequence.clear();
    }

    @Test
    public void test_subscribe_strong() {
        Object o = new Object() {
            @Subscribe(topics = {"prog/events/a", "prog/events/**"}, eventType = String.class)
            private void handle(Event<String> event) {
                sequence.add(event.getSource());
            }
        };
        processor.process(o);
        publish();
        assertEquals(sequence.toString(), "[Hello for a, Hello for a1, Hello for a1]");
    }

    @Test
    public void test_subscribe_weak() {
        Object o = new Object() {
            @Subscribe(topics = {"prog/events/a", "prog/events/b/**"}, eventType = String.class)
            @Reference(WEAK)
            private void handle(Event<String> event) {
                sequence.add(event.getSource());
            }
        };
        processor.process(o);

        System.gc();
        System.gc();
        System.gc();

        publish();
        assertEquals(sequence.toString(), "[]");
    }

    @Test
    public void test_multiple_publish() {
        Object o = new Object() {
            @Subscribe(topics = "prog/events/a/**", eventType = String.class)
            private void handle(Event<String> event) {
                sequence.add(event.getSource());
            }
        };
        processor.process(o);
        publish();
        assertEquals(sequence.toString(), "[Hello for a, Hello for a1, Hello for a1]");
    }

    private void publish() {
        B b = processor.proxy(B.class);
        C c = processor.proxy(C.class);
        b.send("Hello for a", 1);
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
