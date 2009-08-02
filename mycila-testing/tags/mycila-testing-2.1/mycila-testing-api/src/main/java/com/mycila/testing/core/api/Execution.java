/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mycila.testing.core.api;

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
    TestContext context();

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

    /**
     * Handle attributes of this Execution Context, which can be shared amongst plugins
     *
     * @return Attribute handler
     */
    Attributes attributes();
}
