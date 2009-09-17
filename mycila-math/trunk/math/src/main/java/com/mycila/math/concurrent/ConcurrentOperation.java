package com.mycila.math.concurrent;

import com.mycila.math.number.BigInt;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        EXECUTOR_SERVICE = Executors.newFixedThreadPool(cpus, new DefaultThreadFactory());
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

    public static final class Slice extends Generic<BigInt[]> {
        private final int len;

        private Slice(int len) {
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

    public static class Generic<T> {

        private final ExecutorCompletionService<T> service = new ExecutorCompletionService<T>(ConcurrentOperation.EXECUTOR_SERVICE);
        private int count;

        private Generic() {
        }

        public boolean isEmpty() {
            return count == 0;
        }

        public int size() {
            return count;
        }

        public T take() {
            try {
                T t = service.take().get();
                count--;
                return t;
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage(), e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        public void push(Callable<T> callable) {
            count++;
            service.submit(callable);
        }

        public Result<T> result(Callable<T> callable) {
            count++;
            return new Result<T>(service.submit(callable));
        }
    }
}
