package com.mycila.math.concurrent;

import com.mycila.math.number.BigInt;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;

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
        EXECUTOR_SERVICE = Executors.newFixedThreadPool(cpus);
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

}
