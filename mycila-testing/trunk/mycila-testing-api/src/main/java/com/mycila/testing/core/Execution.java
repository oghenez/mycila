package com.mycila.testing.core;

import java.lang.reflect.Method;

/**
 * Represents a current execution of a method in a test
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Execution {

    /**
     * @return The current step of this execution
     */
    Step step();

    /**
     * Get the test method that is going to be executed or has been executed
     *
     * @return The test method
     */
    Method method();

    /**
     * @return The Context of the test instance
     */
    Context context();

    /**
     * @return The exception thrown by the method execution if it failed. Returns null if none
     */
    Throwable throwable();

    /**
     * @return wheter the method has thrown an exception
     */
    boolean hasFailed();

    /**
     * Mark a test execution metod of having failed by providing its exception thrown
     *
     * @param throwable The exception thrown by teh method
     */
    void setThrowable(Throwable throwable);

}
