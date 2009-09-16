package com.mycila.math.concurrent;

import com.mycila.math.number.BigInt;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ConcurrentOperations {

    private static final ExecutorService EXECUTOR_SERVICE;

    static {
        int cpus = Runtime.getRuntime().availableProcessors();
        try {
            cpus = Integer.parseInt(System.getProperty("mycila.math.cpus"));
        } catch (Exception ignored) {
        }
        EXECUTOR_SERVICE = Executors.newFixedThreadPool(cpus, new DefaultThreadFactory());
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                EXECUTOR_SERVICE.shutdownNow();
            }
        });
    }

    private ConcurrentOperations() {

    }

    public static MultiplyOperation multiply() {
        return new MultiplyOperation() {
            ExecutorCompletionService<BigInt> service = new ExecutorCompletionService<BigInt>(EXECUTOR_SERVICE);
            int count;

            @Override
            public Future<BigInt> multiply(final BigInt a, final BigInt b) {
                count++;
                return service.submit(new Callable<BigInt>() {
                    @Override
                    public BigInt call() throws Exception {
                        return a.multiply(b);
                    }
                });
            }

            @Override
            public boolean isEmpty() {
                return count == 0;
            }

            @Override
            public int size() {
                return count;
            }

            @Override
            public BigInt take() {
                try {
                    BigInt bigInt = service.take().get();
                    count--;
                    return bigInt;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e.getMessage(), e);
                } catch (ExecutionException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        };
    }

    static class DefaultThreadFactory implements ThreadFactory {
        private static final int THREAD_PRIORITY;
        private static final AtomicInteger threadNumber = new AtomicInteger(1);

        static {
            int p = Thread.MAX_PRIORITY;
            try {
                p = Integer.parseInt(System.getProperty("mycila.math.thread.priority"));
            } catch (Exception ignored) {
            }
            THREAD_PRIORITY = p;
        }

        private final ThreadGroup group;
        private final String namePrefix;
        private final ClassLoader ccl;

        DefaultThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" + threadNumber.getAndIncrement() + "-thread-";
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

}
