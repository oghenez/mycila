package com.mycila.log.jdk;

import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class StdoutHandler extends ConsoleHandler {

    private final Level max;

    public StdoutHandler() {
        setOutputStream(System.out);
        String val = LogManager.getLogManager().getProperty(getClass().getName() + ".max");
        max = val == null ? Level.SEVERE : Level.parse(val);
    }

    @Override
    public boolean isLoggable(LogRecord record) {
        int level = record.getLevel().intValue();
        return super.isLoggable(record) && (level <= max.intValue() || level == Level.OFF.intValue());
    }
}
