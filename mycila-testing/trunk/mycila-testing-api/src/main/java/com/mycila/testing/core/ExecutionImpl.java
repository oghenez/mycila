package com.mycila.testing.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class ExecutionImpl implements Execution {

    private final Context context;
    private final Method method;
    private Throwable throwable;
    private Step step = Step.UNKNOWN;

    ExecutionImpl(Context context, Method method) {
        this.context = context;
        this.method = method;
    }

    final ExecutionImpl changeStep(Step step) {
        this.step = step;
        return this;
    }

    public final Step step() {
        return step;
    }

    public final Method method() {
        return method;
    }

    public final Context context() {
        return context;
    }

    public final Throwable throwable() {
        return throwable;
    }

    public final boolean hasFailed() {
        return throwable != null;
    }

    public void setThrowable(Throwable throwable) {
        if (throwable != null && throwable instanceof InvocationTargetException) {
            throwable = ((InvocationTargetException) throwable).getTargetException();
        }
        this.throwable = throwable;
    }
}
