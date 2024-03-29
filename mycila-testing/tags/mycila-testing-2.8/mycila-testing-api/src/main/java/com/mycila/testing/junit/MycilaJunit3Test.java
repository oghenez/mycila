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

import com.mycila.log.Logger;
import com.mycila.log.Loggers;
import com.mycila.testing.core.Mycila;
import com.mycila.testing.core.MycilaTesting;
import com.mycila.testing.core.api.TestExecution;
import com.mycila.testing.core.api.TestNotifier;
import com.mycila.testing.core.util.Closeable;
import com.mycila.testing.core.util.ShutdownHook;
import junit.framework.TestCase;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class MycilaJunit3Test extends TestCase {

    private static final Logger LOGGER = Loggers.get(MycilaJunit3Test.class);

    public MycilaJunit3Test() {
        super();
    }

    public MycilaJunit3Test(String name) {
        super(name);
    }

    @Override
    public final void runBare() throws Throwable {
        final TestNotifier testNotifier = MycilaTesting.from(getClass()).configure(this).createNotifier(this);
        ShutdownHook.get().add(new Closeable() {
            public void close() throws Exception {
                testNotifier.shutdown();
            }
        });
        testNotifier.prepare();
        try {
            setUp();
            testNotifier.fireBeforeTest(getTestMethod());
            TestExecution testExecution = (TestExecution) Mycila.currentExecution();
            if (!testExecution.mustSkip()) {
                try {
                    LOGGER.debug("Calling test method %s.%s", testExecution.method().getDeclaringClass().getName(), testExecution.method().getName());
                    super.runTest();
                } catch (Throwable t) {
                    testExecution.setThrowable(t);
                }
            }
            testNotifier.fireAfterTest();
            tearDown();
            if (testExecution.hasFailed()) {
                throw testExecution.throwable().fillInStackTrace();
            }
        } finally {
            testNotifier.fireAfterClass();
        }
    }

    private Method getTestMethod() {
        assertNotNull("TestCase.getName() cannot be null", getName());
        Method testMethod = null;
        try {
            testMethod = getClass().getMethod(getName());
        }
        catch (NoSuchMethodException ex) {
            fail("Method \"" + getName() + "\" not found");
        }
        if (!Modifier.isPublic(testMethod.getModifiers())) {
            fail("Method \"" + getName() + "\" should be public");
        }
        return testMethod;
    }

}
