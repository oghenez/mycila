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

import com.mycila.event.ErrorHandlerProvider;
import com.mycila.event.util.DefaultThreadFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public enum Dispatchers {

    SYNCHRONOUS_SAFE_DISPATCHER {
        @Override
        public Dispatcher create(ErrorHandlerProvider errorHandlerProvider) {
            return new OrderedDispatcher(errorHandlerProvider, new Executor() {
                final Lock publishing = new ReentrantLock();

                @Override
                public void execute(Runnable command) {
                    publishing.lock();
                    try {
                        command.run();
                    } finally {
                        publishing.unlock();
                    }
                }
            });
        }},

    SYNCHRONOUS_UNSAFE_DISPATCHER {
        @Override
        public Dispatcher create(ErrorHandlerProvider errorHandlerProvider) {
            return new OrderedDispatcher(errorHandlerProvider, new Executor() {
                @Override
                public void execute(Runnable command) {
                    command.run();
                }
            });
        }},

    ASYNCHRONOUS_SAFE_DISPATCHER {
        @Override
        public Dispatcher create(ErrorHandlerProvider errorHandlerProvider) {
            ThreadFactory threadFactory = new DefaultThreadFactory(this.name(), "dispatcher");
            Executor executor = new ThreadPoolExecutor(
                    0, 1,
                    20L, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(),
                    threadFactory);
            return new OrderedDispatcher(errorHandlerProvider, executor);
        }},

    ASYNCHRONOUS_UNSAFE_DISPATCHER {
        @Override
        public Dispatcher create(ErrorHandlerProvider errorHandlerProvider) {
            ThreadFactory threadFactory = new DefaultThreadFactory(this.name(), "dispatcher");
            Executor executor = new ThreadPoolExecutor(
                    0, Integer.MAX_VALUE,
                    20L, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(),
                    threadFactory);
            return new OrderedDispatcher(errorHandlerProvider, executor);
        }},

    BROADCAST_ORDERED_DISPATCHER {
        @Override
        public Dispatcher create(ErrorHandlerProvider errorHandlerProvider) {
            return null;
        }},

    BROADCAST_UNORDERED_DISPATCHER {
        @Override
        public Dispatcher create(ErrorHandlerProvider errorHandlerProvider) {
            return null;
        }};

    public abstract Dispatcher create(ErrorHandlerProvider errorHandlerProvider);

}
