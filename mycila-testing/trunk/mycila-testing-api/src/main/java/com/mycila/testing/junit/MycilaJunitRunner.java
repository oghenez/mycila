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

import com.mycila.testing.core.Mycila;
import com.mycila.testing.core.MycilaTesting;
import com.mycila.testing.core.TestExecution;
import com.mycila.testing.core.TestNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class MycilaJunitRunner extends BlockJUnit4ClassRunner {

    private TestNotifier testNotifier;

    public MycilaJunitRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected final Object createTest() throws Exception {
        Object test = super.createTest();
        testNotifier = MycilaTesting.from(getTestClass().getJavaClass()).createNotifier(test);
        return test;
    }

    @Override
    protected final List<FrameworkMethod> computeTestMethods() {
        List<FrameworkMethod> methods = new ArrayList<FrameworkMethod>(super.computeTestMethods());
        methods.add(0, new FrameworkMethod(method("MycilaJunitRunner_prepare")));
        methods.add(new FrameworkMethod(method("MycilaJunitRunner_afterClass")));
        return methods;
    }

    @Override
    protected final Statement methodInvoker(final FrameworkMethod method, final Object test) {
        return new Statement() {
            @SuppressWarnings({"ThrowableResultOfMethodCallIgnored"})
            @Override
            public void evaluate() throws Throwable {
                testNotifier.fireBeforeTest(method.getMethod());
                TestExecution testExecution = (TestExecution) Mycila.currentExecution();
                if (!testExecution.mustSkip()) {
                    try {
                        MycilaJunitRunner.super.methodInvoker(method, test).evaluate();
                    } catch (Throwable t) {
                        testExecution.setThrowable(t);
                    }
                }
                testNotifier.fireAfterTest();
                if (testExecution.throwable() != null) {
                    throw testExecution.throwable();
                }
            }
        };
    }

    private Method method(String methodName) {
        try {
            Method method = MycilaJunitRunner.class.getDeclaredMethod(methodName);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            //noinspection ThrowableInstanceNeverThrown
            throw (AssertionError) new AssertionError("Internal error in Mycila Testing. Please report it to http://code.google.com/p/mycila/issues/list. Exception is: " + e.getMessage()).initCause(e);
        }
    }

    private void MycilaJunitRunner_prepare() {
        testNotifier.prepare();
    }

    private void MycilaJunitRunner_afterClass() {
        testNotifier.fireAfterClass();
    }
}
