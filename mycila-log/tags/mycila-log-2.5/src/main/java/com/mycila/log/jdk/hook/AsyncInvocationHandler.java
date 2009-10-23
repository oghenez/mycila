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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AsyncInvocationHandler<T extends Handler> extends MycilaInvocationHandler<T> {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void publish(final T handler, final LogRecord record) {
        executor.execute(new Runnable() {
            public void run() {
                handler.publish(record);
                handler.flush();
            }
        });
    }

    @Override
    public void close(T handler) throws SecurityException {
        executor.shutdown();
        try {
            while(!executor.isTerminated()) {
                executor.awaitTermination(500, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        handler.flush();
        handler.close();
    }
}
