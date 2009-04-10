package com.mycila.log.jdk.hook;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AsyncInvocationHandler<T extends Handler> extends MycilaInvocationHandler<T> {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void publish(final T handler, final LogRecord record) {
        executor.execute(new Runnable() {
            public void run() {
                handler.publish(record);
                handler.flush();
            }
        });
    }

    @Override
    public void close(T handler) throws SecurityException {
        LogRecord logRecord = new LogRecord(Level.FINE, "Publishing all remaing asynchronous log entries...");
        logRecord.setLoggerName(getClass().getName());
        logRecord.setSourceClassName(getClass().getName());
        logRecord.setSourceMethodName(Thread.currentThread().getName());
        handler.publish(logRecord);
        handler.flush();
        executor.shutdown();
        handler.flush();
        handler.close();
    }
}
