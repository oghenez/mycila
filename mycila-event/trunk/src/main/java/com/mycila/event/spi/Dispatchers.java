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

import com.mycila.event.api.Dispatcher;

import java.util.concurrent.CompletionService;
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
public final class Dispatchers {

    private Dispatchers() {
    }

    public static Dispatcher custom(
            ErrorHandler errorHandler,
            Executor publishExecutor,
            Executor subscriberExecutor) {
        return new DefaultDispatcher(errorHandler, publishExecutor, subscriberExecutor);
    }

    public static Dispatcher synchronousSafe(ErrorHandler errorHandler) {
        return new DefaultDispatcher(errorHandler, Executors.blocking(), Executors.immediate());
    }

    public static Dispatcher synchronousUnsafe(ErrorHandler errorHandler) {
        return new DefaultDispatcher(errorHandler, Executors.immediate(), Executors.immediate());
    }

    public static Dispatcher asynchronousSafe(ErrorHandler errorHandler) {
        ThreadFactory threadFactory = new DefaultThreadFactory("AsynchronousSafe", "dispatcher");
        Executor executor = new ThreadPoolExecutor(
                0, 1,
                10L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                threadFactory);
        return new DefaultDispatcher(errorHandler, executor, Executors.immediate());
    }

    public static Dispatcher asynchronousUnsafe(ErrorHandler errorHandler) {
        ThreadFactory threadFactory = new DefaultThreadFactory("AsynchronousUnsafe", "dispatcher");
        Executor executor = new ThreadPoolExecutor(
                0, Integer.MAX_VALUE,
                10L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                threadFactory);
        return new DefaultDispatcher(errorHandler, executor, Executors.immediate());
    }

    public static Dispatcher broadcastOrdered(ErrorHandler errorHandler) {
        ThreadFactory threadFactory = new DefaultThreadFactory("BroadcastOrdered", "dispatcher");
        final Executor publishingExecutor = new ThreadPoolExecutor(
                0, 1,
                10L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                threadFactory);
        final SubscribersExecutor subscribersExecutor = new SubscribersExecutor(new ThreadPoolExecutor(
                0, Integer.MAX_VALUE,
                10L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                threadFactory));
        return new DefaultDispatcher(
                errorHandler,
                new Executor() {
                    @Override
                    public void execute(final Runnable command) {
                        publishingExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                subscribersExecutor.onPublishStarting();
                                try {
                                    command.run();
                                } finally {
                                    subscribersExecutor.waitForCompletion();
                                }
                            }
                        });
                    }
                },
                subscribersExecutor);
    }

    public static Dispatcher broadcastUnordered(ErrorHandler errorHandler) {
        ThreadFactory threadFactory = new DefaultThreadFactory("BroadcastUnordered", "dispatcher");
        Executor executor = new ThreadPoolExecutor(
                0, Integer.MAX_VALUE,
                10L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                threadFactory);
        return new DefaultDispatcher(errorHandler, executor, executor);
    }

    private static final class SubscribersExecutor implements Executor {

        final Executor executor;
        CompletionService<Void> completionService;
        int count = 0;

        SubscribersExecutor(Executor executor) {
            this.executor = executor;
        }

        @Override
        public void execute(Runnable command) {
            completionService.submit(command, null);
            count++;
        }

        void onPublishStarting() {
            completionService = new ExecutorCompletionService<Void>(executor);
            count = 0;
        }

        void waitForCompletion() {
            try {
                while (count-- > 0)
                    completionService.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }
}
