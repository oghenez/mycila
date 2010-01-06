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
import com.mycila.event.api.ErrorHandler;

import java.util.concurrent.CompletionService;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Dispatchers {

    private Dispatchers() {
    }

    public static Dispatcher custom(ErrorHandler errorHandler,
                                    Executor publishExecutor,
                                    Executor subscriberExecutor) {
        return new DefaultDispatcher(errorHandler, publishExecutor, subscriberExecutor);
    }

    public static Dispatcher custom(ErrorHandler errorHandler,
                                    final ExecutorService publishExecutor,
                                    final ExecutorService subscriberExecutor) {
        return new DefaultDispatcher(errorHandler, publishExecutor, subscriberExecutor) {
            @Override
            public void close() {
                publishExecutor.shutdown();
                subscriberExecutor.shutdown();
            }
        };
    }

    public static Dispatcher synchronousSafe(ErrorHandler errorHandler) {
        return new DefaultDispatcher(errorHandler, Executors.blocking(), Executors.immediate());
    }

    public static Dispatcher synchronousUnsafe(ErrorHandler errorHandler) {
        return new DefaultDispatcher(errorHandler, Executors.immediate(), Executors.immediate());
    }

    public static Dispatcher asynchronousSafe(ErrorHandler errorHandler) {
        final ExecutorService executor = java.util.concurrent.Executors.newSingleThreadExecutor(
                new DefaultThreadFactory("AsynchronousSafe", "dispatcher", false));
        return new DefaultDispatcher(errorHandler, executor, Executors.immediate()) {
            @Override
            public void close() {
                executor.shutdown();
            }
        };
    }

    public static Dispatcher asynchronousUnsafe(ErrorHandler errorHandler) {
        return asynchronousUnsafe(Runtime.getRuntime().availableProcessors() * 4, errorHandler);
    }

    public static Dispatcher asynchronousUnsafe(int corePoolSize, ErrorHandler errorHandler) {
        final ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(
                corePoolSize,
                new DefaultThreadFactory("AsynchronousUnsafe", "dispatcher", false));
        return new DefaultDispatcher(errorHandler, executor, Executors.immediate()) {
            @Override
            public void close() {
                executor.shutdown();
            }
        };
    }

    public static Dispatcher broadcastOrdered(ErrorHandler errorHandler) {
        return broadcastOrdered(Runtime.getRuntime().availableProcessors() * 4, errorHandler);
    }

    public static Dispatcher broadcastOrdered(int corePoolSize, ErrorHandler errorHandler) {
        final ExecutorService publishingExecutor = java.util.concurrent.Executors.newSingleThreadExecutor(
                new DefaultThreadFactory("BroadcastOrdered", "dispatcher", false));
        final ExecutorService subscriberExecutor = java.util.concurrent.Executors.newFixedThreadPool(
                corePoolSize,
                new DefaultThreadFactory("BroadcastOrdered", "dispatcher", false));
        final SubscriberCompletionExecutor subscriberCompletionExecutor = new SubscriberCompletionExecutor(subscriberExecutor);
        return new DefaultDispatcher(errorHandler, new Executor() {
            public void execute(final Runnable command) {
                publishingExecutor.execute(new Runnable() {
                    public void run() {
                        subscriberCompletionExecutor.onPublishStarting();
                        try {
                            command.run();
                        } finally {
                            subscriberCompletionExecutor.waitForCompletion();
                        }
                    }
                });
            }
        }, subscriberCompletionExecutor) {
            @Override
            public void close() {
                publishingExecutor.shutdown();
                subscriberExecutor.shutdown();
            }
        };
    }

    public static Dispatcher broadcastUnordered(ErrorHandler errorHandler) {
        return broadcastUnordered(Runtime.getRuntime().availableProcessors() * 4, errorHandler);
    }

    public static Dispatcher broadcastUnordered(int corePoolSize, ErrorHandler errorHandler) {
        final ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(
                corePoolSize,
                new DefaultThreadFactory("BroadcastUnordered", "dispatcher", false));
        return new DefaultDispatcher(errorHandler, executor, executor) {
            @Override
            public void close() {
                executor.shutdown();
            }
        };
    }

    private static final class SubscriberCompletionExecutor implements Executor {

        final Executor executor;
        CompletionService<Void> completionService;
        int count = 0;

        SubscriberCompletionExecutor(Executor executor) {
            this.executor = executor;
        }

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
