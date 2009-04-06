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

import com.mycila.testing.core.Context;
import com.mycila.testing.core.DefaultTestPlugin;
import com.mycila.testing.core.MycilaTesting;
import com.mycila.testing.core.TestExecution;
import com.mycila.testing.core.TestNotifier;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class TestNGSample3Test extends AbstractMycilaTestNGTest {

    boolean prepared;
    boolean before;
    boolean after;

    boolean test_method;
    boolean test_skip;
    boolean test_fail;

    @AfterMethod
    public void reset() {
        before = false;
        after = false;
    }

    @Test
    public void test_method() {
        test_method = true;
    }

    @Test
    public void test_skip() {
        test_skip = true;
        fail("method should be skipped");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_fail() {
        test_fail = true;
        throw new IllegalArgumentException("hello");
    }

    @Override
    protected TestNotifier getTestHandler() {
        MycilaTesting testSetup = MycilaTesting.newCustomSetup();
        testSetup.pluginManager().getCache().registerPlugin("custom", new DefaultTestPlugin() {
            @Override
            public void beforeTest(TestExecution testExecution) {
                System.out.println("before");
                assertTrue(prepared);
                assertFalse(before);
                before = true;
                testExecution.setSkip(testExecution.getMethod().getName().equals("test_skip"));
            }

            @Override
            public void afterTest(TestExecution testExecution) {
                System.out.println("after");
                assertTrue(prepared);
                assertTrue(before);
                assertFalse(after);
                if (testExecution.getMethod().getName().equals("test_fail")) {
                    System.out.println("=> exception");
                    assertNotNull(testExecution.getThrowable());
                    assertEquals(testExecution.getThrowable().getClass(), IllegalArgumentException.class);
                } else {
                    assertNull(testExecution.getThrowable());
                }
            }

            @Override
            public void prepareTestInstance(Context context) {
                System.out.println("prep");
                assertFalse(prepared);
                prepared = true;
            }

            @Override
            public void afterClass(Context context) {
                System.out.println("end");
                assertTrue(test_method);
                assertFalse(test_skip);
                assertTrue(test_fail);
            }
        });
        return testSetup.createNotifier(this);
    }
}