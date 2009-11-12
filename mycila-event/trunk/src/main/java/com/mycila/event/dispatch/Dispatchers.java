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

package com.mycila.event.dispatch;

import com.mycila.event.ErrorHandler;
import com.mycila.event.ErrorHandlerAdapter;
import com.mycila.event.ErrorHandlers;
import com.mycila.event.util.DefaultThreadFactory;
import com.mycila.event.util.Executors;
import com.mycila.event.util.Provider;
import com.mycila.event.util.Providers;

import java.util.concurrent.CompletionService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public enum Dispatchers {

    SYNCHRONOUS_SAFE_DISPATCHER {
        @Override
        public Dispatcher create(Provider<ErrorHandler> errorHandlerProvider) {
            return new DefaultDispatcher(errorHandlerProvider, Executors.blocking(), Executors.immediate());
        }},

    SYNCHRONOUS_UNSAFE_DISPATCHER {
        @Override
        public Dispatcher create(Provider<ErrorHandler> errorHandlerProvider) {
            return new DefaultDispatcher(errorHandlerProvider, Executors.immediate(), Executors.immediate());
        }},

    ASYNCHRONOUS_SAFE_DISPATCHER {
        @Override
        public Dispatcher create(Provider<ErrorHandler> errorHandlerProvider) {
            ThreadFactory threadFactory = new DefaultThreadFactory(this.name(), "dispatcher");
            Executor executor = new ThreadPoolExecutor(
                    0, 1,
                    20L, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(),
                    threadFactory);
            return new DefaultDispatcher(errorHandlerProvider, Providers.cache(executor), Executors.immediate());
        }},

    ASYNCHRONOUS_UNSAFE_DISPATCHER {
        @Override
        public Dispatcher create(Provider<ErrorHandler> errorHandlerProvider) {
            ThreadFactory threadFactory = new DefaultThreadFactory(this.name(), "dispatcher");
            Executor executor = new ThreadPoolExecutor(
                    0, Integer.MAX_VALUE,
                    20L, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(),
                    threadFactory);
            return new DefaultDispatcher(errorHandlerProvider, Providers.cache(executor), Executors.immediate());
        }},

    BROADCAST_ORDERED_DISPATCHER {
        @Override
        public Dispatcher create(Provider<ErrorHandler> errorHandlerProvider) {
            ThreadFactory threadFactory = new DefaultThreadFactory(this.name(), "dispatcher");
            final Executor executor = new ThreadPoolExecutor(
                    0, Integer.MAX_VALUE,
                    20L, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(),
                    threadFactory);

            return new DefaultDispatcher(ErrorHandlers.compose(Providers.<ErrorHandler>cache(new ErrorHandlerAdapter() {
                @Override
                public void onPublishingStarting() {

                }

                @Override
                public void onPublishingFinished() {
                    com
                }
            }), errorHandlerProvider),
                    Providers.cache(executor),
                    Providers.cache(new Executor() {
                        @Override
                        public void execute(Runnable command) {
                            completionService.submit(command, null);
                        }
                    }));
        }},

    BROADCAST_UNORDERED_DISPATCHER {
        @Override
        public Dispatcher create(Provider<ErrorHandler> errorHandlerProvider) {
            ThreadFactory threadFactory = new DefaultThreadFactory(this.name(), "dispatcher");
            Executor executor = new ThreadPoolExecutor(
                    0, Integer.MAX_VALUE,
                    20L, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(),
                    threadFactory);
            return new DefaultDispatcher(errorHandlerProvider, Providers.cache(executor), Providers.cache(executor));
        }};

    public abstract Dispatcher create(Provider<ErrorHandler> errorHandlerProvider);

    private static final class OrderedBroadcasting extends ErrorHandlerAdapter implements  Executor {

        

        @Override
        public void execute(Runnable command) {
        }

    }

}
