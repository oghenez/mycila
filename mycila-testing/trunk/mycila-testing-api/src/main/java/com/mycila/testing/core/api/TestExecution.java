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

/**
 * Represents a current test method execution. This is a context create when calling
 * {@link com.mycila.testing.core.api.TestNotifier#fireBeforeTest(java.lang.reflect.Method)}
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface TestExecution extends Execution {

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

}
