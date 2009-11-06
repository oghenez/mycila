package com.mycila.sandbox.concurrent.barrier;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class IntBarrier {

    private final Lock modification = new ReentrantLock();
    private volatile int counter;
    private final List<PerThreadBarrier> barriers = new LinkedList<PerThreadBarrier>();

    private IntBarrier(int initialValue) {
        this.counter = initialValue;
    }

    public int count() {
        return counter;
    }

    public int increment() {
        modification.lock();
        try {
            return fire(++counter);
        } finally {
            modification.unlock();
        }
    }

    public int decrement() {
        modification.lock();
        try {
            return fire(--counter);
        } finally {
            modification.unlock();
        }
    }

    private int fire(final int newValue) {
        for (Iterator<PerThreadBarrier> it = barriers.iterator(); it.hasNext();) {
            PerThreadBarrier barrier = it.next();
            if (barrier.value() == newValue) {
                it.remove();
                barrier.release();
            }
        }
        return newValue;
    }

    public void waitFor(int barrier) throws InterruptedException {
        PerThreadBarrier threadBarrier = PerThreadBarrier.releasedAt(barrier);
        barriers.add(threadBarrier);
        threadBarrier.await();
    }

    public void waitFor(int barrier, long time, TimeUnit unit) throws InterruptedException {
        PerThreadBarrier threadBarrier = PerThreadBarrier.releasedAt(barrier);
        barriers.add(threadBarrier);
        threadBarrier.await(time, unit);
    }

    public static IntBarrier zero() {
        return init(0);
    }

    public static IntBarrier init(int initialValue) {
        return new IntBarrier(initialValue);
    }

    private static final class PerThreadBarrier {
        private final int value;
        private final CountDownLatch latch = new CountDownLatch(1);

        private PerThreadBarrier(int value) {
            this.value = value;
        }

        int value() {
            return value;
        }

        void release() {
            latch.countDown();
        }

        public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
            return latch.await(timeout, unit);
        }

        public void await() throws InterruptedException {
            latch.await();
        }

        static PerThreadBarrier releasedAt(int value) {
            return new PerThreadBarrier(value);
        }
    }
}
