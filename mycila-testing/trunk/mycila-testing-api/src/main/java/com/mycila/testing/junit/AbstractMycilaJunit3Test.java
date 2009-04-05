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
import com.mycila.testing.core.TestHandler;
import junit.framework.TestCase;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class AbstractMycilaJunit3Test extends TestCase {

    private TestHandler testHandler;

    public AbstractMycilaJunit3Test() {
        super();
    }

    public AbstractMycilaJunit3Test(String name) {
        super(name);
    }

    @Override
    public final void runBare() throws Throwable {
        testHandler = getTestHandler();
        testHandler.prepare();
        try {
            super.runBare();
        } finally {
            testHandler.end();
        }
    }

    @Override
    protected final void runTest() throws Throwable {
        Method runMethod = getTestMethod();
        Throwable throwable = null;
        try {
            if (testHandler.beforeTest(runMethod)) {
                try {
                    super.runTest();
                } catch (Throwable t) {
                    throwable = t;
                    throw t;
                }
            }
        } finally {
            testHandler.afterTest(runMethod, throwable);
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

    /**
     * Can be overriden to modify the behavior and provide in example our proper TestSetup instance
     *
     * @return A TestHandler to fire test events (before and after execution)
     */
    protected TestHandler getTestHandler() {
        return MycilaTesting.from(getClass()).handle(this);
    }

}
