package com.mycila.event.impl;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class DefaultThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);

    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final String namePrefix;

    DefaultThreadFactory(String poolPrefix, String namePrefix) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.namePrefix = poolPrefix + "-" + poolNumber.getAndIncrement() + "-" + namePrefix + "-";
    }

    @Override
    public Thread newThread(final Runnable r) {
        final ClassLoader ccl = Thread.currentThread().getContextClassLoader();
        final Thread t = new Thread(group, new Runnable() {
            @Override
            public void run() {
                Thread.currentThread().setContextClassLoader(ccl);
                r.run();
            }
        }, namePrefix + threadNumber.getAndIncrement(), 0);
        if (t.isDaemon()) t.setDaemon(false);
        t.setPriority(Thread.currentThread().getPriority());
        return t;
    }
}
