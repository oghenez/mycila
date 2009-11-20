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

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.mycila.event.annotation.Publish;
import com.mycila.event.annotation.Reference;
import com.mycila.event.annotation.SplitEvents;
import com.mycila.event.annotation.Subscribe;
import com.mycila.event.annotation.Veto;
import com.mycila.event.guice.MycilaEventGuiceModule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

import static com.mycila.event.guice.MycilaEventGuice.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class GuiceTest implements Module {

    Publisher<String> publisher;

    @Override
    public void configure(Binder binder) {
        binder.bind(GuiceTest.class).toInstance(this);
        bindPublisher(binder, MyCustomPublisher.class).in(Singleton.class);
        bindPublisher(binder, MyCustomPublisher2.class).in(Singleton.class);
        bindPublisher(binder, MyCustomPublisher3.class).in(Singleton.class);
    }

    @Test
    public void test() throws Exception {
        Module m = new MycilaEventGuiceModule() {
            @Override
            protected Provider<Dispatcher> dispatcher() {
                return new Provider<Dispatcher>() {
                    @Inject
                    Provider<ErrorHandler> errorHandler;

                    @Override
                    public Dispatcher get() {
                        return Dispatchers.synchronousUnsafe(errorHandler.get());
                    }
                };
            }
        };
        Injector injector = Guice.createInjector(this, m);
        injector.getInstance(GuiceTest.class).publisher.publish("Hello world !");
        injector.getInstance(MyCustomPublisher.class).send("A", "cut", "message", "containing", "bad words");
        injector.getInstance(MyCustomPublisher2.class).send(1, "A", "cut", "message", "containing", "bad words", "in varg");
        injector.getInstance(MyCustomPublisher3.class).send(1, Arrays.asList("A", "cut", "message", "containing", "bad words", "in list"));
    }

    @Subscribe(topics = "a/topic/path", eventType = String.class)
    void subscribe(Event<String> event) {
        System.out.println("(subscribe) Got: " + event);
    }

    @Subscribe(topics = "a/topic/path", eventType = String[].class)
    void subscribeToList(Event<String[]> event) {
        System.out.println("(subscribeToList) Got: " + Arrays.toString(event.source()));
    }

    @Subscribe(topics = "a/topic/path", eventType = Integer.class)
    void subscribeToInts(Event<Integer> event) {
        System.out.println("(subscribeToInts) Got: " + event.source());
    }

    @Veto(topics = "a/topic/path", eventType = String.class)
    void vetoer(VetoableEvent<String> vetoableEvent) {
        if (vetoableEvent.event().source().contains("bad words"))
            vetoableEvent.veto();
    }

    @Publish(topics = "a/topic/path")
    void publisher(Publisher<String> publisher) {
        System.out.println("Publisher injected");
        publisher.publish("Hello from publisher !");
        this.publisher = publisher;
    }

    @Reference(Reachability.WEAK)
    static interface MyCustomPublisher {
        @Publish(topics = "a/topic/path")
        void send(String... messages);
    }

    static abstract class MyCustomPublisher2 {
        @Publish(topics = "a/topic/path")
        @SplitEvents
        abstract void send(int event1, String... otherEvents);
    }

    static abstract class MyCustomPublisher3 {
        @Publish(topics = "a/topic/path")
        @SplitEvents
        abstract void send(int event1, Iterable<String> events);
    }
}
