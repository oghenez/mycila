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
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface TestNotifier {
    /**
     * Ask for a test preparation
     *
     * @throws TestPluginException If a plugin failed enhancing the test instance
     */
    void prepare() throws TestPluginException;

    /**
     * Fires a before test exection event to all plugins
     *
     * @param method Test method that will be executed
     * @return True of the execution should be done, false otherwise. The decision is taken by the plugins
     * @throws TestPluginException If a plugin failed in its execution
     */
    TestExecution fireBeforeTest(Method method) throws TestPluginException;

    /**
     * Must be fired after the execution of a test method, even if the test method was skipped because if a return of false of the beforeTest method.
     *
     * @param testExecution The test execution context return by {@link #fireBeforeTest(java.lang.reflect.Method)}
     * @throws TestPluginException If a plugin failed in its execution
     */
    void fireAfterTest(TestExecution testExecution) throws TestPluginException;

    /**
     * Fires an event telling plugins that all tests of this class have ended
     *
     * @throws TestPluginException If a plugin failed in its execution
     */
    void fireAfterClass() throws TestPluginException;

}
