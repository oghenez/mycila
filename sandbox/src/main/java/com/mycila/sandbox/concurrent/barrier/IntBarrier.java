package com.mycila.sandbox.concurrent.barrier;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class IntBarrier {

    private final Lock lock = new ReentrantLock();
    private final Condition barrierReached = lock.newCondition();
    private final AtomicInteger counter;

    private IntBarrier(int initialValue) {
        this.counter = new AtomicInteger(initialValue);
    }

    public int count() {
        return counter.get();
    }

    public int getAndIncrement() {
        return getAndAdd(1);
    }

    public int getAndDecrement() {
        return getAndAdd(-1);
    }

    public int decrementAndGet() {
        return addAndGet(-1);
    }

    public int incrementAndGet() {
        return addAndGet(1);
    }

    private int addAndGet(int delta) {
        lock.lock();
        try {
            int v = counter.addAndGet(delta);
            barrierReached.signalAll();
            return v;
        } finally {
            lock.unlock();
        }
    }

    private int getAndAdd(int delta) {
        lock.lock();
        try {
            int v = counter.getAndAdd(delta);
            barrierReached.signalAll();
            return v;
        } finally {
            lock.unlock();
        }
    }

    public void waitFor(int barrier) throws InterruptedException {
        lock.lock();
        try {
            while (count() < barrier)
                barrierReached.await();
        } finally {
            lock.unlock();
        }
    }

    public void waitFor(int barrier, long time, TimeUnit unit) throws InterruptedException {
        lock.lock();
        try {
            while (count() < barrier)
                barrierReached.await(time, unit);
        } finally {
            lock.unlock();
        }
    }

    public static IntBarrier zero() {
        return init(0);
    }

    public static IntBarrier one() {
        return init(1);
    }

    public static IntBarrier init(int initialValue) {
        return new IntBarrier(initialValue);
    }

}
