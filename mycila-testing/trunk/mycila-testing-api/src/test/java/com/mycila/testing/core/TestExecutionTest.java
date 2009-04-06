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

import com.mycila.testing.util.Code;
import static com.mycila.testing.util.ExtendedAssert.*;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class TestExecutionTest {

    @Test
    public void test_skip() throws Exception {
        TestNotifier notifier = MycilaTesting.from(getClass()).createNotifier(this);

        final TestExecution testExecution = notifier.fireBeforeTest(getClass().getMethod("test_skip"));
        assertTrue(testExecution.mustSkip());

        assertThrow(UnsupportedOperationException.class).withMessage("TestExecution.setSkip can only be called after 'fireBeforeTest' call").whenRunning(new Code() {
            public void run() throws Throwable {
                testExecution.setSkip(true);
            }
        });

        notifier.fireAfterTest(testExecution);

        assertThrow(UnsupportedOperationException.class).withMessage("TestExecution.setSkip can only be called after 'fireBeforeTest' call").whenRunning(new Code() {
            public void run() throws Throwable {
                testExecution.setSkip(true);
            }
        });
    }

    @Test
    public void test_throwable() throws Exception {
        TestNotifier notifier = MycilaTesting.from(getClass()).createNotifier(this);

        final TestExecution testExecution = notifier.fireBeforeTest(getClass().getMethod("test_throwable"));
        assertNull(testExecution.getThrowable());
        assertFalse(testExecution.hasFailed());

        testExecution.setThrowable(new IllegalStateException());
        assertNotNull(testExecution.getThrowable());
        assertTrue(testExecution.hasFailed());

        testExecution.setThrowable(null);
        assertNull(testExecution.getThrowable());
        assertFalse(testExecution.hasFailed());

        testExecution.setThrowable(new InvocationTargetException(new IllegalArgumentException()));
        assertNotNull(testExecution.getThrowable());
        assertEquals(testExecution.getThrowable().getClass(), IllegalArgumentException.class);
        assertTrue(testExecution.hasFailed());
        
        notifier.fireAfterTest(testExecution);

        assertNotNull(testExecution.getThrowable());
        assertTrue(testExecution.hasFailed());

        assertThrow(UnsupportedOperationException.class).withMessage("TestExecution.setThrowable can only be called after 'fireBeforeTest' call and before 'fireAfterTest' call").whenRunning(new Code() {
            public void run() throws Throwable {
                testExecution.setThrowable(new IllegalStateException());
            }
        });
    }
}
