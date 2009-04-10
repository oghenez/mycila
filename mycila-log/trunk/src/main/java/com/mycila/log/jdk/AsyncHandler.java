package com.mycila.log.jdk;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AsyncHandler extends Handler {

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler handler;

    public AsyncHandler(Handler handler) {
        this.handler = handler;
    }

    public void publish(final LogRecord record) {
        executor.execute(new Runnable() {
            public void run() {
                handler.publish(record);
                flush();
            }
        });
    }

    public void flush() {
        handler.flush();
    }

    public void close() throws SecurityException {
        LogRecord logRecord = new LogRecord(Level.FINE, "Publishing all remaing asynchronous log entries...");
        logRecord.setLoggerName(getClass().getName());
        logRecord.setSourceClassName(getClass().getName());
        logRecord.setSourceMethodName(Thread.currentThread().getName());
        handler.publish(logRecord);
        flush();
        executor.shutdown();
        flush();
        handler.close();
    }
}
