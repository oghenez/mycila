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

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.mycila.event.AnnotationProcessor;
import com.mycila.event.Dispatcher;
import com.mycila.event.Dispatchers;
import com.mycila.event.ErrorHandler;
import com.mycila.event.ErrorHandlers;

import static com.google.inject.matcher.Matchers.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class MycilaEventGuiceModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(ErrorHandler.class)
                .toProvider(errorHandler())
                .in(Singleton.class);

        binder.bind(Dispatcher.class)
                .toProvider(dispatcher())
                .in(Singleton.class);

        binder.bind(AnnotationProcessor.class)
                .toProvider(annotationProcessor())
                .in(Singleton.class);

        binder.bindListener(any(), new TypeListener() {
            @Override
            public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {

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

    protected Provider<AnnotationProcessor> annotationProcessor() {
        return new Provider<AnnotationProcessor>() {
            @Inject
            Dispatcher dispatcher;

            @Override
            public AnnotationProcessor get() {
                return AnnotationProcessor.create(dispatcher);
            }
        };
    }

    protected Provider<Dispatcher> dispatcher() {
        return new Provider<Dispatcher>() {
            @Inject
            ErrorHandler errorHandler;

            @Override
            public Dispatcher get() {
                return Dispatchers.broadcastUnordered(errorHandler);
            }
        };
    }
}
