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

package com.mycila.testing.core.plugin;

import com.mycila.plugin.api.Plugin;
import com.mycila.testing.core.api.TestContext;
import com.mycila.testing.core.api.TestExecution;

/**
 * This interface should be implemented by all plugins.
 * <strong>It is strongly adviced that plugins extends {@link DefaultTestPlugin}
 * instead of implementing directly {@link TestPlugin} interface</strong>,
 * to avoid any code break when changing or enhancing the API.
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface TestPlugin extends Plugin {

    /**
     * This method will be called on each plugin in order of dependency to prepare the test instance before
     * all tests begin to be executed.
     *
     * @param context The test context, relative to one test instance
     * @throws Exception If anything wrong occurs in plugin execution. Exception will be catched by the testing API.
     */
    void prepareTestInstance(TestContext context) throws Exception;

    /**
     * This method will be called on each plugin in order of dependency to execute code required before a test method can run.
     *
     * @param testExecution The test execution context of this test method. The text execution enables plugins to control whethere the test method
     *                      should be skipped or not
     * @throws Exception If anything wrong occurs in plugin execution. Exception will be catched by the testing API.
     */
    void beforeTest(TestExecution testExecution) throws Exception;

    /**
     * This method will be called on each plugin in order of dependency after each test method has finished, even if the method failed.
     *
     * @param testExecution The test execution context of this test method. The test execution context enables the plugins to handle what to do
     *                      in case of a failed test, with the provided exception in the context
     * @throws Exception If anything wrong occurs in plugin execution. Exception will be catched by the testing API.
     */
    void afterTest(TestExecution testExecution) throws Exception;

    /**
     * This method will be called on each plugin in order of dependency after all test method of a test instance have finished.
     *
     * @param context The context of this test
     * @throws Exception If anything wrong occurs in plugin execution. Exception will be catched by the testing API.
     */
    void afterClass(TestContext context) throws Exception;
}
