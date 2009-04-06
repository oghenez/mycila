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
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ContextHolderTest {
    @Test
    public void test() throws Exception {
        Context context = mock(Context.class);
        assertThrow(IllegalStateException.class).withMessage("There is not Context bound to local thread !").whenRunning(new Code() {
            public void run() throws Throwable {
                ContextHolder.get();
            }
        });
        ContextHolder.set(context);
        assertEquals(ContextHolder.get(), context);
        ContextHolder.unset();
        assertThrow(IllegalStateException.class).withMessage("There is not Context bound to local thread !").whenRunning(new Code() {
            public void run() throws Throwable {
                ContextHolder.get();
            }
        });
    }
}
