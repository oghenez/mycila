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
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import static java.util.Arrays.*;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@MycilaPlugins(value = Cache.UNSHARED, descriptor = "")
public final class TestNGFlowTest extends MycilaTestNGTest {

    static {
        JDKLogging.init();
    }

    List<String> flow = new ArrayList<String>();

    @BeforeMethod
    public void before() throws Exception {
        flow.add("before");
    }

    @Test
    public void method1() throws Exception {
        flow.add("method1");
        assertEquals(flow, asList("prepare", "before", "beforeTest", "method1"), flow.toString());
    }

    @Test(dependsOnMethods = "method1")
    public void method2() throws Exception {
        flow.add("method2");
        assertEquals(flow, asList("prepare", "before", "beforeTest", "method1", "afterTest", "after", "before", "beforeTest", "method2"), flow.toString());
    }

    @Test(dependsOnMethods = "method2")
    public void method3() throws Exception {
        flow.add("method3");
        fail("SHOULD NOT BE EXECUTED - should be skipped - method3 should not be added in flow");
    }

    @Test(dependsOnMethods = "method3", expectedExceptions = AssertionError.class)
    public void method4() throws Exception {
        flow.add("method4");
        assertEquals(flow, asList("prepare", "before", "beforeTest", "method1", "afterTest", "after", "before", "beforeTest", "method2", "afterTest", "after", "before", "beforeTest", "afterTest", "after", "before", "beforeTest", "method4"), flow.toString());
        throw new AssertionError("METHOD 4 ERROR");
    }

    @AfterMethod
    public void after() throws Exception {
        flow.add("after");
    }

    @ConfigureMycilaPlugins
    void configure(PluginManager<TestPlugin> pluginManager) {
        pluginManager.getCache().registerPlugin("myPlugin", new TestPlugin() {
            public void prepareTestInstance(Context context) throws Exception {
                flow.add("prepare");
                assertEquals(flow, asList("prepare"), flow.toString());
            }

            public void beforeTest(TestExecution testExecution) throws Exception {
                flow.add("beforeTest");
                if (testExecution.method().getName().equals("method1")) {
                    assertEquals(flow, asList("prepare", "before", "beforeTest"), flow.toString());
                } else if (testExecution.method().getName().equals("method2")) {
                    assertEquals(flow, asList("prepare", "before", "beforeTest", "method1", "afterTest", "after", "before", "beforeTest"), flow.toString());
                } else if (testExecution.method().getName().equals("method3")) {
                    assertEquals(flow, asList("prepare", "before", "beforeTest", "method1", "afterTest", "after", "before", "beforeTest", "method2", "afterTest", "after", "before", "beforeTest"), flow.toString());
                    testExecution.setSkip(true);
                } else if (testExecution.method().getName().equals("method4")) {
                    assertEquals(flow, asList("prepare", "before", "beforeTest", "method1", "afterTest", "after", "before", "beforeTest", "method2", "afterTest", "after", "before", "beforeTest", "afterTest", "after", "before", "beforeTest"), flow.toString());
                }
            }

            public void afterTest(final TestExecution testExecution) throws Exception {
                flow.add("afterTest");
                if (testExecution.method().getName().equals("method1")) {
                    assertNull(testExecution.throwable());
                    assertEquals(flow, asList("prepare", "before", "beforeTest", "method1", "afterTest"), flow.toString());
                } else if (testExecution.method().getName().equals("method2")) {
                    assertNull(testExecution.throwable());
                    assertEquals(flow, asList("prepare", "before", "beforeTest", "method1", "afterTest", "after", "before", "beforeTest", "method2", "afterTest"), flow.toString());
                } else if (testExecution.method().getName().equals("method3")) {
                    assertNull(testExecution.throwable());
                    assertTrue(testExecution.mustSkip());
                    assertEquals(flow, asList("prepare", "before", "beforeTest", "method1", "afterTest", "after", "before", "beforeTest", "method2", "afterTest", "after", "before", "beforeTest", "afterTest"), flow.toString());
                } else if (testExecution.method().getName().equals("method4")) {
                    assertNotNull(testExecution.throwable());
                    assertThrow(AssertionError.class).withMessage("METHOD 4 ERROR").whenRunning(new Code() {
                        public void run() throws Throwable {
                            throw testExecution.throwable();
                        }
                    });
                    assertEquals(flow, asList("prepare", "before", "beforeTest", "method1", "afterTest", "after", "before", "beforeTest", "method2", "afterTest", "after", "before", "beforeTest", "afterTest", "after", "before", "beforeTest", "method4", "afterTest"), flow.toString());
                }
            }

            public void afterClass(Context context) throws Exception {
                flow.add("afterClass");
                assertEquals(flow, asList("prepare", "before", "beforeTest", "method1", "afterTest", "after", "before", "beforeTest", "method2", "afterTest", "after", "before", "beforeTest", "afterTest", "after", "before", "beforeTest", "method4", "afterTest", "after", "afterClass"), flow.toString());

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
