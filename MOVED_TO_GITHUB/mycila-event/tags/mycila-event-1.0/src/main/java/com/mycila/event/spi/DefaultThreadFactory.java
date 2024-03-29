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

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import static com.mycila.event.api.Ensure.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class DefaultThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);

    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final String namePrefix;
    private final String poolPrefix;

    DefaultThreadFactory(String poolPrefix, String namePrefix) {
        notNull(poolPrefix, "Thread pool prefix");
        notNull(namePrefix, "Thread name prefix");
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.poolPrefix = poolPrefix + "-" + poolNumber.getAndIncrement() + "-";
        this.namePrefix = namePrefix;
    }

    @Override
    public Thread newThread(final Runnable r) {
        return newThread(namePrefix, r);
    }

    public Thread newThread(String name, final Runnable runnable) {
        notNull(runnable, "Runnable");
        final ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        final Thread t = new Thread(group, new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setContextClassLoader(ccl);
                runnable.run();
            }
        }, poolPrefix + name + "-" + threadNumber.getAndIncrement(), 0);
        if (t.isDaemon()) t.setDaemon(false);
        t.setPriority(Thread.currentThread().getPriority());
        return t;
    }
}
