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

import com.mycila.plugin.spi.PluginManager;
import com.mycila.testing.JDKLogging;
import com.mycila.testing.core.Cache;
import com.mycila.testing.core.ConfigureMycilaPlugins;
import com.mycila.testing.core.Context;
import com.mycila.testing.core.MycilaPlugins;
import com.mycila.testing.core.TestExecution;
import com.mycila.testing.core.TestPlugin;
import com.mycila.testing.ea.Code;
import static com.mycila.testing.ea.ExtendedAssert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import static java.util.Arrays.*;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@MycilaPlugins(value = Cache.UNSHARED, descriptor = "")
public final class Junit4FlowTest extends MycilaJunit4Test {

    static {
        JDKLogging.init();
    }

    List<String> flow = new ArrayList<String>();

    @Before
    public void before() throws Exception {
        flow.add("before");
    }

    @Test
    public void method1() throws Exception {
        flow.add("method1");
        assertEquals(flow.toString(), asList("prepare", "before", "beforeTest", "method1"), flow);
    }

    @Test
    public void method2() throws Exception {
        flow.add("method2");
        assertEquals(flow.toString(), asList("prepare", "before", "beforeTest", "method2"), flow);
    }

    @Test
    public void method3() throws Exception {
        flow.add("method3");
        fail("SHOULD NOT BE EXECUTED - should be skipped - method3 should not be added in flow");
    }

    @Test

    public void method4() throws Exception {
        flow.add("method4");
        assertEquals(flow.toString(), asList("prepare", "before", "beforeTest", "method4"), flow);
        throw new AssertionError("METHOD 4 ERROR");
    }

    @After
    public void after() throws Exception {
        flow.add("after");
    }

    @ConfigureMycilaPlugins
    void configure(PluginManager<TestPlugin> pluginManager) {
        pluginManager.getCache().registerPlugin("myPlugin", new TestPlugin() {
            public void prepareTestInstance(Context context) throws Exception {
                flow.add("prepare");
                assertEquals(flow.toString(), asList("prepare"), flow);
            }

            public void beforeTest(TestExecution testExecution) throws Exception {
                flow.add("beforeTest");
                System.out.println("beforeTest: " + testExecution.method().getName());
                assertEquals(flow.toString(), asList("prepare", "before", "beforeTest"), flow);
                if (testExecution.method().getName().equals("method3")) {
                    testExecution.setSkip(true);
                }
            }

            public void afterTest(final TestExecution testExecution) throws Exception {
                flow.add("afterTest");
                System.out.println("afterTest: " + testExecution.method().getName());
                if (testExecution.method().getName().equals("method1")) {
                    assertNull(testExecution.throwable());
                    assertEquals(flow.toString(), asList("prepare", "before", "beforeTest", "method1", "afterTest"), flow);
                } else if (testExecution.method().getName().equals("method2")) {
                    assertNull(testExecution.throwable());
                    assertEquals(flow.toString(), asList("prepare", "before", "beforeTest", "method2", "afterTest"), flow);
                } else if (testExecution.method().getName().equals("method3")) {
                    assertNull(testExecution.throwable());
                    assertTrue(testExecution.mustSkip());
                    assertEquals(flow.toString(), asList("prepare", "before", "beforeTest", "afterTest"), flow);
                } else if (testExecution.method().getName().equals("method4")) {
                    assertNotNull(testExecution.throwable());
                    assertThrow(AssertionError.class).withMessage("METHOD 4 ERROR").whenRunning(new Code() {
                        public void run() throws Throwable {
                            throw testExecution.throwable();
                        }
                    });
                    assertEquals(flow.toString(), asList("prepare", "before", "beforeTest", "method4", "afterTest"), flow);

                    // Just to pass the test and not rethrow the exception to junit
                    testExecution.setThrowable(null);
                }
            }

            public void afterClass(Context context) throws Exception {
                flow.add("afterClass");
                //skipped
                assertFalse(flow.contains("method3"));
                flow.remove("method1");
                flow.remove("method2");
                flow.remove("method4");
                assertEquals(flow.toString(), asList("prepare", "before", "beforeTest", "afterTest", "after", "afterClass"), flow);
            }

            public List<String> getBefore() {
                return null;
            }

            public List<String> getAfter() {
                return null;
            }
        });
    }

}