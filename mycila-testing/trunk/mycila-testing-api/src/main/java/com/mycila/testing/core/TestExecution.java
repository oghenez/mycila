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
