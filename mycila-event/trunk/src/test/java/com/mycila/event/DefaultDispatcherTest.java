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

import com.mycila.event.annotation.Reference;
import com.mycila.event.dispatch.Dispatcher;
import com.mycila.event.dispatch.Dispatchers;
import com.mycila.event.ref.Reachability;
import com.mycila.event.util.Provider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.mycila.event.Topics.*;
import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class DefaultDispatcherTest {

    private final List<Object> sequence = new ArrayList<Object>();

    private Dispatcher dispatcher;

    @Before
    public void setup() {
        dispatcher = Dispatchers.SYNCHRONOUS_UNSAFE_DISPATCHER.create(ErrorHandlers.rethrowErrorsAfterPublish());
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

    @Test
    public void test_SYNCHRONOUS_SAFE_DISPATCHER() throws InterruptedException {
        Provider<ErrorHandler> provider = ErrorHandlers.rethrowErrorsAfterPublish();
        final Dispatcher dispatcher = Dispatchers.SYNCHRONOUS_SAFE_DISPATCHER.create(provider);

        final CountDownLatch go = new CountDownLatch(1);
        final CountDownLatch finished = new CountDownLatch(20);

        for (int i = 0; i < 20; i++) {
            new Thread("T" + i) {
                @Override
                public void run() {
                    try {
                        go.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    dispatcher.publish(Topics.topic("a/b"), Thread.currentThread().getName());
                    finished.countDown();
                }
            }.start();
        }

        final AtomicBoolean inProcess = new AtomicBoolean(false);

        for (int i = 0; i < 20; i++) {
            final int index = i;
            dispatcher.subscribe(Topics.only("a/b"), String.class, new Subscriber<String>() {
                @Override
                public void onEvent(Event<String> event) throws Exception {
                    if (inProcess.get())
                        fail();
                    inProcess.set(true);
                    Thread.sleep(50);
                    System.out.println("[" + Thread.currentThread().getName() + "] " + " S" + index + " - " + event.source());
                    inProcess.set(false);
                }
            });
        }

        go.countDown();
        finished.await();
    }

    @Test
    public void test_SYNCHRONOUS_UNSAFE_DISPATCHER() throws InterruptedException {
        Provider<ErrorHandler> provider = ErrorHandlers.rethrowErrorsAfterPublish();
        final Dispatcher dispatcher = Dispatchers.SYNCHRONOUS_UNSAFE_DISPATCHER.create(provider);

        final CountDownLatch go = new CountDownLatch(1);
        final CountDownLatch finished = new CountDownLatch(20);

        for (int i = 0; i < 20; i++) {
            new Thread("T" + i) {
                @Override
                public void run() {
                    try {
                        go.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    dispatcher.publish(Topics.topic("a/b"), Thread.currentThread().getName());
                    finished.countDown();
                }
            }.start();
        }

        final AtomicBoolean inProcess = new AtomicBoolean(false);
        final AtomicInteger paralellCalls = new AtomicInteger(0);

        for (int i = 0; i < 20; i++) {
            final int index = i;
            dispatcher.subscribe(Topics.only("a/b"), String.class, new Subscriber<String>() {
                @Override
                public void onEvent(Event<String> event) throws Exception {
                    if (inProcess.get())
                        paralellCalls.incrementAndGet();
                    inProcess.set(true);
                    Thread.sleep(200);
                    System.out.println("[" + Thread.currentThread().getName() + "] " + " S" + index + " - " + event.source());
                    inProcess.set(false);
                }
            });
        }

        go.countDown();
        finished.await();

        System.out.println("paralellCalls.get()=" + paralellCalls.get());
        assertTrue(paralellCalls.get() > 10);
    }

    @Test
    public void test_ASYNCHRONOUS_SAFE_DISPATCHER() throws InterruptedException {
        Provider<ErrorHandler> provider = ErrorHandlers.rethrowErrorsAfterPublish();
        final Dispatcher dispatcher = Dispatchers.ASYNCHRONOUS_SAFE_DISPATCHER.create(provider);

        final CountDownLatch go = new CountDownLatch(1);
        final CountDownLatch publish = new CountDownLatch(20);
        final CountDownLatch consume = new CountDownLatch(20 * 20);

        for (int i = 0; i < 20; i++) {
            new Thread("T" + i) {
                @Override
                public void run() {
                    try {
                        go.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    dispatcher.publish(Topics.topic("a/b"), Thread.currentThread().getName());
                    publish.countDown();
                }
            }.start();
        }

        final AtomicBoolean inProcess = new AtomicBoolean(false);
        final AtomicInteger paralellCalls = new AtomicInteger(0);

        for (int i = 0; i < 20; i++) {
            final int index = i;
            dispatcher.subscribe(Topics.only("a/b"), String.class, new Subscriber<String>() {
                @Override
                public void onEvent(Event<String> event) throws Exception {
                    if (inProcess.get())
                        paralellCalls.incrementAndGet();
                    inProcess.set(true);
                    Thread.sleep(10);
                    System.out.println("[" + Thread.currentThread().getName() + "] " + " S" + index + " - " + event.source());
                    inProcess.set(false);
                    consume.countDown();
                }
            });
        }

        go.countDown();
        publish.await();

        System.out.println("publishing finished, waiting for consumers...");

        consume.await();

        System.out.println("paralellCalls.get()=" + paralellCalls.get());
        assertEquals(paralellCalls.get(), 0);
    }

    @Test
    public void test_ASYNCHRONOUS_UNSAFE_DISPATCHER() throws InterruptedException {
        Provider<ErrorHandler> provider = ErrorHandlers.rethrowErrorsAfterPublish();
        final Dispatcher dispatcher = Dispatchers.ASYNCHRONOUS_UNSAFE_DISPATCHER.create(provider);

        final CountDownLatch go = new CountDownLatch(1);
        final CountDownLatch publish = new CountDownLatch(20);
        final CountDownLatch consume = new CountDownLatch(20 * 20);

        for (int i = 0; i < 20; i++) {
            new Thread("T" + i) {
                @Override
                public void run() {
                    try {
                        go.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    dispatcher.publish(Topics.topic("a/b"), Thread.currentThread().getName());
                    publish.countDown();
                }
            }.start();
        }

        final AtomicBoolean inProcess = new AtomicBoolean(false);
        final AtomicInteger paralellCalls = new AtomicInteger(0);

        for (int i = 0; i < 20; i++) {
            final int index = i;
            dispatcher.subscribe(Topics.only("a/b"), String.class, new Subscriber<String>() {
                @Override
                public void onEvent(Event<String> event) throws Exception {
                    if (inProcess.get())
                        paralellCalls.incrementAndGet();
                    inProcess.set(true);
                    Thread.sleep(100);
                    System.out.println("[" + Thread.currentThread().getName() + "] " + " S" + index + " - " + event.source());
                    inProcess.set(false);
                    consume.countDown();
                }
            });
        }

        go.countDown();
        publish.await();

        System.out.println("publishing finished, waiting for consumers...");

        consume.await();

        System.out.println("paralellCalls.get()=" + paralellCalls.get());
        assertTrue(paralellCalls.get() > 10);
    }

    @Test
    public void test_BROADCAST_ORDERED_DISPATCHER() throws InterruptedException {
        Provider<ErrorHandler> provider = ErrorHandlers.rethrowErrorsAfterPublish();
        final Dispatcher dispatcher = Dispatchers.BROADCAST_ORDERED_DISPATCHER.create(provider);

        final CountDownLatch go = new CountDownLatch(1);
        final CountDownLatch publish = new CountDownLatch(20);
        final CountDownLatch consume = new CountDownLatch(20 * 20);

        for (int i = 0; i < 20; i++) {
            new Thread("T" + i) {
                @Override
                public void run() {
                    try {
                        go.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    dispatcher.publish(Topics.topic("a/b"), Thread.currentThread().getName());
                    publish.countDown();
                }
            }.start();
        }

        final AtomicBoolean inProcess = new AtomicBoolean(false);
        final AtomicInteger paralellCalls = new AtomicInteger(0);

        for (int i = 0; i < 20; i++) {
            final int index = i;
            dispatcher.subscribe(Topics.only("a/b"), String.class, new Subscriber<String>() {
                @Override
                public void onEvent(Event<String> event) throws Exception {
                    if (inProcess.get())
                        paralellCalls.incrementAndGet();
                    inProcess.set(true);
                    Thread.sleep(100);
                    System.out.println("[" + Thread.currentThread().getName() + "] " + " S" + index + " - " + event.source());
                    inProcess.set(false);
                    consume.countDown();
                }
            });
        }

        go.countDown();
        publish.await();

        System.out.println("publishing finished, waiting for consumers...");

        consume.await();

        System.out.println("paralellCalls.get()=" + paralellCalls.get());
        assertTrue(paralellCalls.get() > 30);
    }

    @Test
    public void test_BROADCAST_UNORDERED_DISPATCHER() throws InterruptedException {
        Provider<ErrorHandler> provider = ErrorHandlers.rethrowErrorsAfterPublish();
        final Dispatcher dispatcher = Dispatchers.BROADCAST_UNORDERED_DISPATCHER.create(provider);

        final CountDownLatch go = new CountDownLatch(1);
        final CountDownLatch publish = new CountDownLatch(20);
        final CountDownLatch consume = new CountDownLatch(20 * 20);

        for (int i = 0; i < 20; i++) {
            new Thread("T" + i) {
                @Override
                public void run() {
                    try {
                        go.await();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    dispatcher.publish(Topics.topic("a/b"), Thread.currentThread().getName());
                    publish.countDown();
                }
            }.start();
        }

        final AtomicBoolean inProcess = new AtomicBoolean(false);
        final AtomicInteger paralellCalls = new AtomicInteger(0);

        for (int i = 0; i < 20; i++) {
            final int index = i;
            dispatcher.subscribe(Topics.only("a/b"), String.class, new Subscriber<String>() {
                @Override
                public void onEvent(Event<String> event) throws Exception {
                    if (inProcess.get())
                        paralellCalls.incrementAndGet();
                    inProcess.set(true);
                    Thread.sleep(100);
                    System.out.println("[" + Thread.currentThread().getName() + "] " + " S" + index + " - " + event.source());
                    inProcess.set(false);
                    consume.countDown();
                }
            });
        }

        go.countDown();
        publish.await();

        System.out.println("publishing finished, waiting for consumers...");

        consume.await();

        System.out.println("paralellCalls.get()=" + paralellCalls.get());
        assertTrue(paralellCalls.get() > 30);
    }

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
