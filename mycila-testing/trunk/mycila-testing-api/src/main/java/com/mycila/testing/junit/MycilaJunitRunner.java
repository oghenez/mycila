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
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class MycilaJunitRunner extends BlockJUnit4ClassRunner {

    private static final Logger LOGGER = Loggers.get(MycilaJunitRunner.class);

    private TestNotifier testNotifier;

    public MycilaJunitRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected final Object createTest() throws Exception {
        Object test = super.createTest();
        testNotifier = MycilaTesting.from(test.getClass()).configure(test).createNotifier(test);
        ShutdownHook.get().add(new Closeable() {
            public void close() throws Exception {
                testNotifier.shutdown();
            }
        });
        testNotifier.prepare();
        return test;
    }

    @Override
    protected Statement classBlock(RunNotifier notifier) {
        final Statement statement = super.classBlock(notifier);
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                try {
                    statement.evaluate();
                } finally {
                    if (testNotifier != null) {
                        testNotifier.fireAfterClass();
                    }
                }
            }
        };
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
                        LOGGER.debug("Calling test method %s.%s", testExecution.method().getDeclaringClass().getName(), testExecution.method().getName());
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

}
