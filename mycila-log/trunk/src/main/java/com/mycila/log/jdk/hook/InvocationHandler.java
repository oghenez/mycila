package com.mycila.log.jdk.hook;

import java.io.UnsupportedEncodingException;
import java.util.logging.ErrorManager;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface InvocationHandler<T extends Handler> {

    void publish(T handler, LogRecord record);

    void flush(T handler);

    void close(T handler) throws SecurityException;

    void setFormatter(T handler, Formatter newFormatter) throws SecurityException;

    Formatter getFormatter(T handler);

    void setEncoding(T handler, String encoding) throws SecurityException, UnsupportedEncodingException;

    String getEncoding(T handler);

    void setFilter(T handler, Filter newFilter) throws SecurityException;

    Filter getFilter(T handler);

    void setErrorManager(T handler, ErrorManager em);

    ErrorManager getErrorManager(T handler);

    void setLevel(T handler, Level newLevel) throws SecurityException;

    Level getLevel(T handler);

    boolean isLoggable(T handler, LogRecord record);
}
