package com.mycila.log.jdk.handler;

import com.mycila.log.jdk.format.ClassFormatter;

import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class StderrHandler extends MycilaHandler<StreamHandler> {
    public StderrHandler() {
        super(new StreamHandler(System.err, new ClassFormatter()) {
            @Override
            public void publish(LogRecord record) {
                super.publish(record);
                flush();
            }

            @Override
            public void close() {
                flush();
            }
        });
    }
}
