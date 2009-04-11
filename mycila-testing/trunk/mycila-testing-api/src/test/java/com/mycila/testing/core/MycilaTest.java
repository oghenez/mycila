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

import static com.mycila.testing.core.Cache.*;
import com.mycila.testing.testng.MycilaTestNGTest;
import com.mycila.testing.util.Code;
import static com.mycila.testing.util.ExtendedAssert.*;
import static org.mockito.Mockito.*;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@MycilaPlugins(UNSHARED)
public final class MycilaTest extends MycilaTestNGTest {
    @Test
    public void test() throws Exception {
        TestInstance testInstance = new TestInstance(this);
        Context context = mock(Context.class);
        when(context.test()).thenReturn(testInstance);
        assertThrow(IllegalStateException.class).withMessage("There is no Context bound to local thread !").whenRunning(new Code() {
            public void run() throws Throwable {
                Mycila.context(MycilaTest.this);
            }
        });
        Mycila.registerContext(context);
        assertEquals(Mycila.currentExecution(), context);
        Mycila.unsetCurrentExecution();
        assertThrow(IllegalStateException.class).withMessage("There is no Context bound to local thread !").whenRunning(new Code() {
            public void run() throws Throwable {
                Mycila.context(MycilaTest.this);
            }
        });
    }
}
