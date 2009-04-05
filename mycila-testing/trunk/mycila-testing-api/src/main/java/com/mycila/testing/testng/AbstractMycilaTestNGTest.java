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

import com.mycila.testing.core.MycilaTesting;
import com.mycila.testing.core.TestHandler;
import org.testng.Assert;
import org.testng.IHookCallBack;
import org.testng.IHookable;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class AbstractMycilaTestNGTest extends Assert implements IHookable {

    private TestHandler testHandler;

    @BeforeClass(alwaysRun = true)
    protected final void prepareTestInstance() {
        testHandler = getTestHandler();
        testHandler.prepare();
    }

    public final void run(IHookCallBack callBack, ITestResult testResult) {
        if (testHandler.beforeTest(testResult.getMethod().getMethod())) {
            callBack.runTestMethod(testResult);
        } else {
            testResult.setStatus(ITestResult.SKIP);
        }
        //noinspection ThrowableResultOfMethodCallIgnored
        testHandler.afterTest(testResult.getMethod().getMethod(), testResult.getThrowable());
    }

    @AfterClass(alwaysRun = true)
    protected final void end() {
        testHandler.end();
    }

    /**
     * Can be overriden to modify the behavior and provide in example our proper TestSetup instance
     *
     * @return A TestHandler to fire test events (before and after execution)
     */
    protected TestHandler getTestHandler() {
        return MycilaTesting.from(getClass()).handle(this);
    }

}
