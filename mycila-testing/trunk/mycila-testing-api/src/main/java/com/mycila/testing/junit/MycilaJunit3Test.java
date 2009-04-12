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
import com.mycila.testing.core.TestExecution;
import com.mycila.testing.core.TestNotifier;
import junit.framework.TestCase;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class MycilaJunit3Test extends TestCase {

    private static final Logger LOGGER = Loggers.get(MycilaJunit3Test.class);

    private TestNotifier testNotifier;

    public MycilaJunit3Test() {
        super();
    }

    public MycilaJunit3Test(String name) {
        super(name);
    }

    @Override
    public final void runBare() throws Throwable {
        testNotifier = MycilaTesting.from(getClass()).configure(this).createNotifier(this);
        testNotifier.prepare();
        try {
            super.runBare();
        } finally {
            testNotifier.fireAfterClass();
        }
    }

    @Override
    protected final void runTest() throws Throwable {
        testNotifier.fireBeforeTest(getTestMethod());
        TestExecution testExecution = (TestExecution) Mycila.currentExecution();
        if (!testExecution.mustSkip()) {
            try {
                LOGGER.debug("Calling test method {0}.{1}", testExecution.method().getDeclaringClass().getName(), testExecution.method().getName());
                super.runTest();
            } catch (Throwable t) {
                testExecution.setThrowable(t);
            }
        }
        testNotifier.fireAfterTest();
        //noinspection ThrowableResultOfMethodCallIgnored
        if (testExecution.throwable() != null) {
            throw testExecution.throwable();
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
