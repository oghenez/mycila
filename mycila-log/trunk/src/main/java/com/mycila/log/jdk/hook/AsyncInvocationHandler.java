/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
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
package com.mycila.log.jdk.hook;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AsyncInvocationHandler<T extends Handler> extends MycilaInvocationHandler<T> implements Runnable {

    private final BlockingQueue<Runnable> records = new LinkedBlockingQueue<Runnable>();
    private final Thread logger = new Thread(this, AsyncInvocationHandler.class.getSimpleName() + "-Thread");
    private final AtomicBoolean running = new AtomicBoolean(true);

    {
        logger.setDaemon(true);
        logger.start();
    }

    @Override
    public void run() {
        while (running.get() && !Thread.currentThread().isInterrupted()) {
            try {
                Runnable r = records.poll(10, TimeUnit.SECONDS);
                if (r != null) r.run();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void publish(final T handler, final LogRecord record) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                handler.publish(record);
                handler.flush();
            }
        };
        if (running.get()) records.offer(r);
        else r.run();
    }

    @Override
    public void close(T handler) throws SecurityException {
        if (running.getAndSet(false)) {
            logger.interrupt();
            List<Runnable> remaing = new LinkedList<Runnable>();
            records.drainTo(remaing);
            for (Runnable runnable : remaing)
                runnable.run();
        }
    }
}
