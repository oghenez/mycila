package com.mycila.sandbox.concurrent;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class WaitingCounter {

    final ReentrantLock lock = new ReentrantLock();
    final Condition barrierReached = lock.newCondition();
    final AtomicInteger value;
    final int barrier;

    public WaitingCounter(int initial, int barrier) {
        this.barrier = barrier;
        value = new AtomicInteger(initial);
    }

    public int increment() {
        lock.lock();
        try {
            int val = value.incrementAndGet();
            barrierReached.signalAll();
            return val;
        } finally {
            lock.unlock();
        }
    }

    public int decrement() {
        lock.lock();
        try {
            int val = value.decrementAndGet();
            barrierReached.signalAll();
            return val;
        } finally {
            lock.unlock();
        }
    }

    public int getBarrier() {
        return barrier;
    }

    public int getValue() {
        return value.get();
    }

    public void waitForBarrierReached() throws InterruptedException {
        lock.lock();
        try {
            while (getValue() != getBarrier())
                barrierReached.await();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public String toString() {
        int b = getBarrier();
        int v = getValue();
        return "WaitingCounter{" + "barrier=" + b + ", value=" + v + '}';
    }
}
