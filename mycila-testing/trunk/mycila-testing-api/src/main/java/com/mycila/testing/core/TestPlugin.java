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

import com.mycila.plugin.api.Plugin;

import java.lang.reflect.Method;

/**
 * This interface should be implemented by all plugins.
 * <strong>It is strongly adviced that plugins extends {@link DefaultTestPlugin}
 * instead of implementing directly {@link com.mycila.testing.core.TestPlugin} interface</strong>,
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
     * @throws TestPluginException If anything wrong occurs during initalization, plugins should throw this exception
     */
    void prepareTestInstance(Context context) throws TestPluginException;

    /**
     * This method will be called on each plugin in order of dependency to execute code required before a test method can run.
     *
     * @param context The context of this test
     * @param method  The method that is going to be executed
     * @return A flag indicating wheter the test method will be executed or not. By default, majority of plugins would return true.
     *         But this flag could be used in situations where you would like to skip some tests
     */
    boolean beforeTest(Context context, Method method);

    /**
     * This method will be called on each plugin in order of dependency after each test method has finished, even if the method failed.
     *
     * @param context   The context of this test
     * @param method    The method that has been executed
     * @param throwable If the test failed, the exception is passed to the method. If the test succeed, the throwable will be null.
     */
    void afterTest(Context context, Method method, Throwable throwable);
}
