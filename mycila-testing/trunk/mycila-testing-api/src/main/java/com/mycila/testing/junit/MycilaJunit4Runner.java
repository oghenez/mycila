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

package com.mycila.testing.junit;

import com.mycila.testing.core.MycilaTesting;
import com.mycila.testing.core.TestExecution;
import com.mycila.testing.core.TestNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class MycilaJunit4Runner extends BlockJUnit4ClassRunner {

    private TestNotifier testNotifier;

    public MycilaJunit4Runner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected Object createTest() throws Exception {
        Object test = super.createTest();
        testNotifier = MycilaTesting.from(getTestClass().getJavaClass()).createNotifier(test);
        return test;
    }

    @Override
    protected Statement methodInvoker(final FrameworkMethod method, final Object test) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                TestExecution testExecution = testNotifier.fireBeforeTest(method.getMethod());
                if (!testExecution.mustSkip()) {
                    try {
                        MycilaJunit4Runner.super.methodInvoker(method, test).evaluate();
                    } catch (Throwable t) {
                        testExecution.setThrowable(t);
                    }
                }
                testNotifier.fireAfterTest(testExecution);
                if (testExecution.hasFailed()) {
                    throw testExecution.getThrowable();
                }
            }
        };
    }

}
