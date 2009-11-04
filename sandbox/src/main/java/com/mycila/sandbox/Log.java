package com.mycila.sandbox;

import java.sql.Timestamp;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Log {
    private Log() {
    }

    public static synchronized void log(Object msg, Object... args) {
        System.out.println(String.format("%s\t[%s] %s",
                new Timestamp(System.currentTimeMillis()),
                Thread.currentThread().getName(),
                String.format(String.valueOf(msg), args)));
    }

}
