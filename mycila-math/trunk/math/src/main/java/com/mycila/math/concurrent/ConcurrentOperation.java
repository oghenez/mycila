package com.mycila.math.concurrent;

import com.mycila.math.number.BigInt;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ConcurrentOperation {

    static final ExecutorService EXECUTOR_SERVICE;

    static {
        int cpus = Runtime.getRuntime().availableProcessors();
        try {
            cpus = Integer.parseInt(System.getProperty("mycila.math.cpus"));
        } catch (Exception ignored) {
        }
        EXECUTOR_SERVICE = new ThreadPoolExecutor(
                cpus, Integer.MAX_VALUE,
                30, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new DefaultThreadFactory("concurrent-operation"),
                new ThreadPoolExecutor.AbortPolicy());
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                EXECUTOR_SERVICE.shutdownNow();
            }
        });
    }

    private ConcurrentOperation() {
    }

    public static <T> Generic<T> generic() {
        return new Generic<T>();
    }

    public static Multiply multiply() {
        return new Multiply();
    }

    public static Slices slices(int len) {
        return new Slices(len);
    }

    public static Slice slice(int len) {
        return new Slice(len);
    }

    public static final class Multiply extends Generic<BigInt> {
        private Multiply() {
        }

        public void push(final BigInt a, final BigInt b) {
            push(new Callable<BigInt>() {
                @Override
                public BigInt call() throws Exception {
                    return a.multiply(b);
                }
            });
        }

        public Result<BigInt> result(final BigInt a, final BigInt b) {
            return result(new Callable<BigInt>() {
                @Override
                public BigInt call() throws Exception {
                    return a.multiply(b);
                }
            });
        }
    }

    public static final class Slices extends Generic<BigInt[]> {
        private final int len;

        private Slices(int len) {
            this.len = len;
        }

        public int length() {
            return len;
        }

        public Result<BigInt[]> result(final BigInt val) {
            return result(new Callable<BigInt[]>() {
                @Override
                public BigInt[] call() throws Exception {
                    return val.slice(len);
                }
            });
        }
    }

    public static final class Slice extends Generic<BigInt> {
        private final int len;

        private Slice(int len) {
            this.len = len;
        }

        public int length() {
            return len;
        }

        public Result<BigInt> result(final BigInt val, final int index) {
            return result(new Callable<BigInt>() {
                @Override
                public BigInt call() throws Exception {
                    return val.slice(len, index);
                }
            });
        }
    }

    public static class Generic<T> {

        private final ExecutorCompletionService<T> service = new ExecutorCompletionService<T>(ConcurrentOperation.EXECUTOR_SERVICE);
        private AtomicInteger count = new AtomicInteger(0);

        private Generic() {
        }

        public boolean isEmpty() {
            return size() == 0;
        }

        public int size() {
            return count.get();
        }

        public T take() {
            try {
                T t = service.take().get();
                count.decrementAndGet();
                return t;
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage(), e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        public void push(Callable<T> callable) {
            count.incrementAndGet();
            service.submit(callable);
        }

        public Result<T> result(Callable<T> callable) {
            count.incrementAndGet();
            return new Result<T>(service.submit(callable));
        }
    }
}
