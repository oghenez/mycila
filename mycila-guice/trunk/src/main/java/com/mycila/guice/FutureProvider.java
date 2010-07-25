package com.mycila.guice;

import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.ProvisionException;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class FutureProvider<T> extends FutureTask<T> implements Provider<T> {
    private final Queue<Key<?>> queue;
    private final Key<T> key;

    FutureProvider(Key<T> key, final Provider<T> unscoped, Queue<Key<?>> executionQueue) {
        super(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return unscoped.get();
            }
        });
        this.key = key;
        this.queue = executionQueue;
        executionQueue.offer(key);
    }

    @Override
    protected void done() {
        queue.remove(key);
    }

    @Override
    public T get() {
        try {
            if (!isDone()) run();
            return super.get();
        } catch (ExecutionException e) {
            throw (RuntimeException) e.getCause();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ProvisionException("Interrupted during provision for key = " + key);
        }
    }

}
