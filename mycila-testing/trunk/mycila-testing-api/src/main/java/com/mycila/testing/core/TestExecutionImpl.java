package com.mycila.testing.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class TestExecutionImpl implements TestExecution {

    static enum State {
        BEFORE_TEST, TEST, AFTER_TEST, FINISHED
    }

    private final Context context;
    private final Method method;
    private boolean mustSkip;
    private Throwable throwable;
    State state = State.BEFORE_TEST;

    TestExecutionImpl(Context context, Method method) {
        this.context = context;
        this.method = method;
    }

    public void setSkip(boolean mustSkip) {
        if (state != State.BEFORE_TEST) {
            throw new UnsupportedOperationException("TestExecution.setSkip can only be called after 'fireBeforeTest' call");
        }
        this.mustSkip = mustSkip;
    }

    public void setThrowable(Throwable throwable) {
        if (state != State.TEST) {
            throw new UnsupportedOperationException("TestExecution.setThrowable can only be called after 'fireBeforeTest' call and before 'fireAfterTest' call");
        }
        if (throwable != null && throwable instanceof InvocationTargetException) {
            throwable = ((InvocationTargetException) throwable).getTargetException();
        }
        this.throwable = throwable;
    }

    public Method getMethod() {
        return method;
    }

    public Context getContext() {
        return context;
    }

    public boolean mustSkip() {
        return mustSkip;
    }

    public boolean hasFailed() {
        return throwable != null;
    }

    public Throwable getThrowable() {
        return throwable;
    }

}
