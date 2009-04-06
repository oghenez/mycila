package com.mycila.testing.core;

import java.lang.reflect.Method;

/**
 * represents a current test method execution. This is a sort of context returned by
 * {@link com.mycila.testing.core.TestNotifier#fireBeforeTest(java.lang.reflect.Method)}
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface TestExecution {
    /**
     * Get the test method that is going to be executed or has been executed
     *
     * @return The test method
     */
    Method getMethod();

    /**
     * @return The Context of the test instance
     */
    Context getContext();

    /**
     * @return wheter the test method should be skipped
     */
    boolean mustSkip();

    /**
     * Control wheter you want to skip this test method
     *
     * @param mustSkip wheter you want to skip the test
     */
    void setSkip(boolean mustSkip);

    /**
     * @return wheter the test has failed
     */
    boolean hasFailed();

    /**
     * @return Teh exception thrown by the test if it failed. Returns null if none
     */
    Throwable getThrowable();

    /**
     * Mark a test execution metod of having failed by providing its exception thrown
     *
     * @param throwable The exception thrown by teh method
     */
    void setThrowable(Throwable throwable);

}
