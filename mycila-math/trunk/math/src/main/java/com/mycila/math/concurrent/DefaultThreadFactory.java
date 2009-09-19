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
package com.mycila.math.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class DefaultThreadFactory implements ThreadFactory {
    private static final int THREAD_PRIORITY;
    private static final AtomicInteger threadNumber = new AtomicInteger(1);

    static {
        int p = Thread.NORM_PRIORITY;
        try {
            p = Integer.parseInt(System.getProperty("mycila.math.thread.priority"));
        } catch (Exception ignored) {
        }
        THREAD_PRIORITY = p;
    }

    private final ThreadGroup group;
    private final String namePrefix;
    private final ClassLoader ccl;

    DefaultThreadFactory(String name) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        namePrefix = name + "-" + threadNumber.getAndIncrement();
        this.ccl = Thread.currentThread().getContextClassLoader();
    }

    @Override
    public Thread newThread(final Runnable r) {
        Thread t = new Thread(group, new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setContextClassLoader(ccl);
                r.run();
            }
        }, namePrefix + threadNumber.getAndIncrement(), 0);
        if (t.isDaemon()) t.setDaemon(false);
        t.setPriority(THREAD_PRIORITY);
        return t;
    }
}