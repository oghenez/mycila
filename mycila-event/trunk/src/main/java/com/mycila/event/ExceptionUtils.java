package com.mycila.event;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class ExceptionUtils {

    private ExceptionUtils() {
    }

    static RuntimeException toRuntime(Throwable e) {
        if (e instanceof InvocationTargetException)
            e = ((InvocationTargetException) e).getTargetException();
        if (e instanceof RuntimeException)
            return (RuntimeException) e;
        DispatcherException wrapped = new DispatcherException(e.getMessage(), e);
        wrapped.setStackTrace(e.getStackTrace());
        return wrapped;
    }

}
