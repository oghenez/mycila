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

package com.mycila.event.integration;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.ScopedBindingBuilder;
import com.mycila.event.api.Dispatcher;
import com.mycila.event.api.ErrorHandlers;
import com.mycila.event.api.Event;
import com.mycila.event.api.Reachability;
import com.mycila.event.api.annotation.AnnotationProcessor;
import com.mycila.event.api.annotation.Multiple;
import com.mycila.event.api.annotation.Publish;
import com.mycila.event.api.annotation.Reference;
import com.mycila.event.api.annotation.Subscribe;
import com.mycila.event.integration.guice.MycilaEventGuiceModule;
import com.mycila.event.spi.AnnotationProcessors;
import com.mycila.event.spi.Dispatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;

import static com.mycila.event.integration.guice.MycilaEventGuice.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class GuiceTest implements Module {

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
            protected ScopedBindingBuilder bindAnnotationProcessor(AnnotatedBindingBuilder<AnnotationProcessor> bindAnnotationProcessor) {
                return bindAnnotationProcessor.toProvider(new Provider<AnnotationProcessor>() {
                    @Inject
                    Provider<Dispatcher> dispatcher;

                    @Override
                    public AnnotationProcessor get() {
                        return AnnotationProcessors.create(dispatcher.get());
                    }
                });
            }

            @Override
            protected ScopedBindingBuilder bindDispatcher(AnnotatedBindingBuilder<Dispatcher> bindDispatcher) {
                return bindDispatcher.toProvider(new Provider<Dispatcher>() {
                    @Override
                    public Dispatcher get() {
                        return Dispatchers.synchronousUnsafe(ErrorHandlers.rethrow());
                    }
                });
            }
        };
        Injector injector = Guice.createInjector(this, m);
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
        System.out.println("(subscribeToList) Got: " + Arrays.toString(event.getSource()));
    }

    @Subscribe(topics = "a/topic/path", eventType = Integer.class)
    void subscribeToInts(Event<Integer> event) {
        System.out.println("(subscribeToInts) Got: " + event.getSource());
    }

    @Reference(Reachability.WEAK)
    static interface MyCustomPublisher {
        @Publish(topics = "a/topic/path")
        void send(String... messages);
    }

    static abstract class MyCustomPublisher2 {
        @Publish(topics = "a/topic/path")
        @Multiple
        abstract void send(int event1, String... otherEvents);
    }

    static abstract class MyCustomPublisher3 {
        @Publish(topics = "a/topic/path")
        @Multiple
        abstract void send(int event1, Iterable<String> events);
    }
}
