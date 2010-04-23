package com.mycila.jmx.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ExceptionUtils {

    private ExceptionUtils() {
    }

    public static Exception rethrowOrWrap(Throwable e) {
        if (e instanceof Error) throw (Error) e;
        if (e instanceof InvocationTargetException) e = ((InvocationTargetException) e).getTargetException();
        if (e instanceof RuntimeException) throw (RuntimeException) e;
        if (e instanceof Exception) return (Exception) e;
        Exception ee = new Exception(e.getMessage(), e);
        ee.setStackTrace(e.getStackTrace());
        return ee;
    }

    public static RuntimeException rethrow(Throwable e) {
        if (e instanceof Error) throw (Error) e;
        if (e instanceof InvocationTargetException) e = ((InvocationTargetException) e).getTargetException();
        if (e instanceof RuntimeException) throw (RuntimeException) e;
        RuntimeException ee = new RuntimeException(e.getMessage(), e);
        ee.setStackTrace(e.getStackTrace());
        throw ee;
    }

    public static String asString(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        pw.close();
        return sw.toString();
    }
}
