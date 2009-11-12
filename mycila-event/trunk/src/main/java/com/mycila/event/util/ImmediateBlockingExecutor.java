package com.mycila.event.util;

import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ImmediateBlockingExecutor implements Executor {
    private final Lock running = new ReentrantLock();

    @Override
    public void execute(Runnable command) {
        running.lock();
        try {
            command.run();
        } finally {
            running.unlock();
        }
    }
}