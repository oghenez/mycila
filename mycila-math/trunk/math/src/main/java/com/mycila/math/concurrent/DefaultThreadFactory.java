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