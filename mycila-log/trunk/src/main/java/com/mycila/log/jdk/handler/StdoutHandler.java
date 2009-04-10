package com.mycila.log.jdk.handler;

import com.mycila.log.jdk.format.ClassFormatter;

import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class StdoutHandler extends MycilaHandler<StreamHandler> {
    public StdoutHandler() {
        super(new StreamHandler(System.out, new ClassFormatter()) {
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
