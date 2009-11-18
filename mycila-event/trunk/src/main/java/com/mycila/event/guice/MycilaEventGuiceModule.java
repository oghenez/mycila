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

package com.mycila.event.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.mycila.event.AnnotationProcessor;
import com.mycila.event.Dispatcher;
import com.mycila.event.Dispatchers;
import com.mycila.event.ErrorHandler;
import com.mycila.event.ErrorHandlers;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

import static com.google.inject.matcher.Matchers.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class MycilaEventGuiceModule extends AbstractModule {

    private final AtomicReference<ConcurrentLinkedQueue<Object>> references = new AtomicReference<ConcurrentLinkedQueue<Object>>();
    private final Processor processor = new Processor() {
        @Inject
        AnnotationProcessor annotationProcessor;

        @Override
        public <I> void process(I instance) {
            ConcurrentLinkedQueue<Object> r = references.get();
            if (r != null) r.offer(instance);
            else annotationProcessor.process(instance);
        }

        @Inject
        void init(Injector injector) {
            ConcurrentLinkedQueue<Object> r = references.getAndSet(null);
            while (!r.isEmpty())
                annotationProcessor.process(r.poll());
        }
    };

    @Override
    public void configure() {
        bind(Processor.class).toInstance(processor);
        bindListener(any(), new TypeListener() {
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

    @Provides
    @Singleton
    protected ErrorHandler errorHandler() {
        return ErrorHandlers.rethrowErrorsImmediately();
    }

    @Provides
    @Singleton
    protected AnnotationProcessor annotationProcessor(Dispatcher dispatcher) {
        return AnnotationProcessor.create(dispatcher);
    }

    @Provides
    @Singleton
    protected Dispatcher dispatcher(ErrorHandler errorHandler) {
        return Dispatchers.broadcastUnordered(errorHandler);
    }

    private static interface Processor {
        <I> void process(I instance);
    }
}
