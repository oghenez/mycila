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

import com.mycila.event.api.DispatcherException;

import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Executors {

    private Executors() {
    }

    static Executor immediate() {
        return new Executor() {
            public void execute(Runnable command) {
                command.run();
            }
        };
    }

    static Executor blocking() {
        return new Executor() {
            public synchronized void execute(Runnable command) {
                command.run();
            }
        };
    }

    public static Executor blocking(final long blockingTimeout, final TimeUnit unit) {
        return new Executor() {
            private final Lock lock = new ReentrantLock();

            public void execute(Runnable command) {
                boolean acquired;
                try {
                    acquired = lock.tryLock(blockingTimeout, unit);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new DispatcherException(e);
                }
                if (acquired) {
                    try {
                        command.run();
                    } finally {
                        lock.unlock();
                    }
                } else throw new DispatcherException(new TimeoutException("Unable to acquire lock in " + blockingTimeout + " " + unit));
            }
        };
    }
}
