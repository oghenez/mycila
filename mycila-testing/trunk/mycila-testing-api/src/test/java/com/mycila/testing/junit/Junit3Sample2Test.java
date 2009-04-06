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

import com.mycila.testing.core.Context;
import com.mycila.testing.core.DefaultTestPlugin;
import com.mycila.testing.core.MycilaTesting;
import com.mycila.testing.core.TestExecution;
import com.mycila.testing.core.TestNotifier;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */

public final class Junit3Sample2Test extends AbstractMycilaJunit3Test {

    boolean prepared;
    boolean before;
    boolean after;

    boolean test_method;
    boolean test_skip;
    boolean test_fail;

    public void test_method() {
        test_method = true;
    }

    public void test_skip() {
        test_skip = true;
        fail("method should be skipped");
    }

    public void _disabled_from_automatic_suite__test_fail() {
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
            public void afterClass(Context context) throws Exception {
                System.out.println("end");
                if (getName().equals("test_skip")) {
                    assertFalse(test_skip);
                } else {
                    assertTrue((Boolean) Junit3Sample2Test.class.getDeclaredField(getName()).get(Junit3Sample2Test.this));
                }
            }
        });
        return testSetup.createNotifier(this);
    }

}