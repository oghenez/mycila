package com.mycila.testing.junit;

import com.mycila.testing.core.Cache;
import com.mycila.testing.core.Context;
import com.mycila.testing.core.MycilaPlugins;
import com.mycila.testing.core.MycilaTesting;
import com.mycila.testing.core.TestExecution;
import com.mycila.testing.core.TestNotifier;
import com.mycila.testing.core.TestPlugin;
import com.mycila.testing.util.Code;
import static com.mycila.testing.util.ExtendedAssert.*;

import java.util.ArrayList;
import static java.util.Arrays.*;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@MycilaPlugins(value = Cache.UNSHARED, descriptor = "")
public final class Junit3FlowTest extends MycilaJunit3Test {

    List<String> flow = new ArrayList<String>();

    @Override
    protected void setUp() throws Exception {
        flow.add("setUp");
    }

    public void test_method1() throws Exception {
        flow.add("method1");
        assertEquals(flow.toString(), asList("prepare", "setUp", "beforeTest", "method1"), flow);
    }

    public void test_method2() throws Exception {
        flow.add("method2");
        assertEquals(flow.toString(), asList("prepare", "setUp", "beforeTest", "method2"), flow);
    }

    public void test_method3() throws Exception {
        flow.add("method3");
        fail("SHOULD NOT BE EXECUTED - should be skipped - method3 should not be added in flow");
    }

    public void test_method4() throws Exception {
        flow.add("method4");
        assertEquals(flow.toString(), asList("prepare", "setUp", "beforeTest", "method4"), flow);
        throw new AssertionError("METHOD 4 ERROR");
    }

    @Override
    protected void tearDown() throws Exception {
        flow.add("tearDown");
    }

    @Override
    protected TestNotifier createTestNotifier() {
        MycilaTesting mycilaTesting = MycilaTesting.from(this);
        mycilaTesting.pluginManager().getCache().registerPlugin("myPlugin", new TestPlugin() {
            public void prepareTestInstance(Context context) throws Exception {
                flow.add("prepare");
                assertEquals(flow.toString(), asList("prepare"), flow);
            }

            public void beforeTest(TestExecution testExecution) throws Exception {
                flow.add("beforeTest");
                System.out.println("beforeTest: " + testExecution.method().getName());
                assertEquals(flow.toString(), asList("prepare", "setUp", "beforeTest"), flow);
                if (testExecution.method().getName().equals("test_method3")) {
                    testExecution.setSkip(true);
                }
            }

            public void afterTest(final TestExecution testExecution) throws Exception {
                flow.add("afterTest");
                System.out.println("afterTest: " + testExecution.method().getName());
                if (testExecution.method().getName().equals("test_method1")) {
                    assertNull(testExecution.throwable());
                    assertEquals(flow.toString(), asList("prepare", "setUp", "beforeTest", "method1", "afterTest"), flow);
                } else if (testExecution.method().getName().equals("test_method2")) {
                    assertNull(testExecution.throwable());
                    assertEquals(flow.toString(), asList("prepare", "setUp", "beforeTest", "method2", "afterTest"), flow);
                } else if (testExecution.method().getName().equals("test_method3")) {
                    assertNull(testExecution.throwable());
                    assertTrue(testExecution.mustSkip());
                    assertEquals(flow.toString(), asList("prepare", "setUp", "beforeTest", "afterTest"), flow);
                } else if (testExecution.method().getName().equals("test_method4")) {
                    assertNotNull(testExecution.throwable());
                    assertThrow(AssertionError.class).withMessage("METHOD 4 ERROR").whenRunning(new Code() {
                        public void run() throws Throwable {
                            throw testExecution.throwable();
                        }
                    });
                    assertEquals(flow.toString(), asList("prepare", "setUp", "beforeTest", "method4", "afterTest"), flow);

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
                assertEquals(flow.toString(), asList("prepare", "setUp", "beforeTest", "afterTest", "tearDown", "afterClass"), flow);
            }

            public List<String> getBefore() {
                return null;
            }

            public List<String> getAfter() {
                return null;
            }
        });
        return mycilaTesting.createNotifier(this);
    }

}