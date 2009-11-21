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

package com.mycila.event.integration.guice;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.mycila.event.api.Dispatcher;
import com.mycila.event.spi.Annotations;
import com.mycila.event.spi.Dispatchers;
import com.mycila.event.spi.ErrorHandler;
import com.mycila.event.spi.ErrorHandlers;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.inject.matcher.Matchers.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class MycilaEventGuiceModule implements Module {

    private final AtomicReference<ConcurrentLinkedQueue<Object>> references = new AtomicReference<ConcurrentLinkedQueue<Object>>(new ConcurrentLinkedQueue<Object>());

    private final Processor processor = new Processor() {
        @Inject
        Provider<Annotations> annotationProcessor;

        @Override
        public <I> void process(I instance) {
            ConcurrentLinkedQueue<Object> r = references.get();
            if (r != null) r.offer(instance);
            else annotationProcessor.get().process(instance);
        }

        @Inject
        void init(Injector injector) {
            ConcurrentLinkedQueue<Object> r = references.getAndSet(null);
            while (!r.isEmpty())
                annotationProcessor.get().process(r.poll());
        }
    };

    @Override
    public final void configure(Binder binder) {
        binder.bind(ErrorHandler.class)
                .toProvider(errorHandler())
                .in(Singleton.class);
        binder.bind(Dispatcher.class)
                .toProvider(dispatcher())
                .in(Singleton.class);
        binder.bind(Annotations.class)
                .toProvider(annotationProcessor())
                .in(Singleton.class);
        binder.bind(Processor.class)
                .toInstance(processor);
        binder.bindListener(any(), new TypeListener() {
            @Override
            public <I> void hear(TypeLiteral<I> type, final TypeEncounter<I> encounter) {
                encounter.register(new InjectionListener<I>() {
                    @Override
                    public void afterInjection(I injectee) {
                        processor.process(injectee);
                    }
                });
            }
        });
    }

    protected Provider<ErrorHandler> errorHandler() {
        return new Provider<ErrorHandler>() {
            @Override
            public ErrorHandler get() {
                return ErrorHandlers.rethrowErrorsImmediately();
            }
        };
    }

    protected Provider<Annotations> annotationProcessor() {
        return new Provider<Annotations>() {
            @Inject
            Provider<Dispatcher> dispatcher;

            @Override
            public Annotations get() {
                return Annotations.create(dispatcher.get());
            }
        };
    }

    protected Provider<Dispatcher> dispatcher() {
        return new Provider<Dispatcher>() {
            @Inject
            Provider<ErrorHandler> errorHandler;

            @Override
            public Dispatcher get() {
                return Dispatchers.broadcastUnordered(errorHandler.get());
            }
        };
    }

    private static interface Processor {
        <I> void process(I instance);
    }
}
