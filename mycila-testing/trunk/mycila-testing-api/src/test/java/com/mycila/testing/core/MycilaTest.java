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

import com.mycila.testing.JDKLogging;
import static com.mycila.testing.core.Cache.*;
import com.mycila.testing.ea.Code;
import static com.mycila.testing.ea.ExtendedAssert.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@MycilaPlugins(UNSHARED)
public final class MycilaTest {

    static {
        JDKLogging.init();
    }

    @Test
    public void test_context() throws Exception {
        TestInstance testInstance = new TestInstance(this);
        Context context = mock(Context.class);
        when(context.test()).thenReturn(testInstance);

        assertThrow(IllegalStateException.class).containingMessage("No Global Test Context available for test com.mycila.testing.core.MycilaTest#").whenRunning(new Code() {
            public void run() throws Throwable {
                Mycila.context(MycilaTest.this);
            }
        });

        Mycila.registerContext(context);
        assertEquals(Mycila.context(this), context);
        Mycila.unsetContext(this);

        assertThrow(IllegalStateException.class).containingMessage("No Global Test Context available for test com.mycila.testing.core.MycilaTest#").whenRunning(new Code() {
            public void run() throws Throwable {
                Mycila.context(MycilaTest.this);
            }
        });
    }

    @Test
    public void test_execution() throws Exception {
        TestInstance testInstance = new TestInstance(this);
        Context context = mock(Context.class);
        final Execution execution = mock(Execution.class);
        when(context.test()).thenReturn(testInstance);
        when(execution.context()).thenReturn(context);
        when(execution.step()).thenReturn(Step.BEFORE);
        when(execution.method()).thenReturn(getClass().getDeclaredMethod("test_execution"));

        assertThrow(IllegalStateException.class).containingMessage("No Execution context bound to local thread !").whenRunning(new Code() {
            public void run() throws Throwable {
                Mycila.currentExecution();
            }
        });

        Mycila.registerCurrentExecution(execution);
        assertEquals(Mycila.currentExecution(), execution);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new Runnable() {
            public void run() {
                // Execution can be inherited between threads
                assertEquals(Mycila.currentExecution(), execution);
            }
        }).get();
        executorService.shutdown();

        Mycila.unsetCurrentExecution();

        assertThrow(IllegalStateException.class).containingMessage("No Execution context bound to local thread !").whenRunning(new Code() {
            public void run() throws Throwable {
                Mycila.currentExecution();
            }
        });
    }
}
