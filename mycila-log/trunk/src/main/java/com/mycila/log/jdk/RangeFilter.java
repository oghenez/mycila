package com.mycila.log.jdk;

import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class RangeFilter implements Filter {

    private Level min;
    private Level max;

    public RangeFilter() {
        min = Level.ALL;
        max = Level.SEVERE;
    }

    public RangeFilter(Level min, Level max) {
        this.min = min;
        this.max = max;
    }

    public boolean isLoggable(LogRecord record) {
        return record.getLevel().intValue() >= min.intValue() && record.getLevel().intValue() <= max.intValue();
    }

    public Level getMin() {
        return min;
    }

    public void setMin(Level min) {
        this.min = min;
    }

    public Level getMax() {
        return max;
    }

    public void setMax(Level max) {
        this.max = max;
    }
}
