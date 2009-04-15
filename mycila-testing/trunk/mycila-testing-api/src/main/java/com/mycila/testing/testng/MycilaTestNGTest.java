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
package com.mycila.testing.testng;

import com.mycila.log.Logger;
import com.mycila.log.Loggers;
import com.mycila.testing.core.Mycila;
import com.mycila.testing.core.MycilaTesting;
import com.mycila.testing.core.api.TestExecution;
import com.mycila.testing.core.api.TestNotifier;
import org.testng.Assert;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class MycilaTestNGTest extends Assert implements IHookable {

    private static final Logger LOGGER = Loggers.get(MycilaTestNGTest.class);

    private TestNotifier testNotifier;

    @BeforeClass(alwaysRun = true)
    protected final void prepareTestInstance() {
        testNotifier = MycilaTesting.from(getClass()).configure(this).createNotifier(this);
        testNotifier.prepare();
    }

    public final void run(IHookCallBack callBack, ITestResult testResult) {
        testNotifier.fireBeforeTest(testResult.getMethod().getMethod());
        TestExecution testExecution = (TestExecution) Mycila.currentExecution();
        if (testExecution.mustSkip()) {
            testResult.setStatus(ITestResult.SKIP);
        } else {
            LOGGER.debug("Calling test method {0}.{1}", testExecution.method().getDeclaringClass().getName(), testExecution.method().getName());
            callBack.runTestMethod(testResult);
        }
        //noinspection ThrowableResultOfMethodCallIgnored
        testExecution.setThrowable(testResult.getThrowable());
        testNotifier.fireAfterTest();
    }

    @AfterClass(alwaysRun = true)
    protected final void end() {
        testNotifier.fireAfterClass();
    }

}
