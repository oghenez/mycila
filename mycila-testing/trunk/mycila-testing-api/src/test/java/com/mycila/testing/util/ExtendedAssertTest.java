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
package com.mycila.testing.util;

import com.mycila.testing.ea.Code;
import static com.mycila.testing.ea.ExtendedAssert.*;
import static org.testng.Assert.*;
import static org.testng.Assert.fail;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ExtendedAssertTest {

    @Test
    public void test_assertNotEquals() throws Exception {
        assertNotEquals("", null);
        assertNotEquals(null, "");
        assertNotEquals(new Object(), new Object());
        try {
            assertNotEquals(null, null);
            fail();
        } catch (AssertionError e) {
            assertEquals(e.getMessage(), "expected:<Objects not equals> but was:<null>");
        }
        try {
            assertNotEquals("aa", "aa");
            fail();
        } catch (AssertionError e) {
            assertEquals(e.getMessage(), "expected:<Objects not equals> but was:<aa>");
        }
    }

    @Test
    public void test_assertEmpty() throws Exception {
        assertBlank(null);
        assertBlank("");
        assertBlank("  ");
        assertEmpty((String) null);
        assertEmpty("");
        assertEmpty((Object[]) null);
        assertEmpty(new Object[0]);
        assertEmpty((Collection<?>) null);
        assertEmpty(Collections.emptySet());
    }

    @Test
    public void test_assertThrow_exception() throws Exception {

        // ok
        assertThrow(RuntimeException.class).whenRunning(new Code() {
            public void run() throws Throwable {
                throw new RuntimeException("hello");
            }
        });

        // no exception thrown when expected
        try {
            assertThrow(RuntimeException.class).whenRunning(new Code() {
                public void run() throws Throwable {
                }
            });
            fail();
        } catch (AssertionError e) {
            e.printStackTrace();
        }

        // bad exception thrown
        try {
            assertThrow(IllegalArgumentException.class).whenRunning(new Code() {
                public void run() throws Throwable {
                    throw new IllegalStateException();
                }
            });
            fail();
        } catch (AssertionError e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_assertThrow_exception_withMessage() throws Exception {

        // ok
        assertThrow(RuntimeException.class).withMessage("hello").whenRunning(new Code() {
            public void run() throws Throwable {
                throw new RuntimeException("hello");
            }
        });

        // ok - null message
        assertThrow(RuntimeException.class).withMessage(null).whenRunning(new Code() {
            public void run() throws Throwable {
                throw new RuntimeException();
            }
        });

        // no exception thrown
        try {
            assertThrow(RuntimeException.class).withMessage("Hello !").whenRunning(new Code() {
                public void run() throws Throwable {
                }
            });
            fail();
        } catch (AssertionError e) {
            e.printStackTrace();
        }

        // message expected, got null
        try {
            assertThrow(RuntimeException.class).withMessage("hello").whenRunning(new Code() {
                public void run() throws Throwable {
                    throw new RuntimeException();
                }
            });
            fail();
        } catch (AssertionError e) {
            e.printStackTrace();
        }

        // null expected, got message
        try {
            assertThrow(RuntimeException.class).withMessage(null).whenRunning(new Code() {
                public void run() throws Throwable {
                    throw new RuntimeException("dd");
                }
            });
            fail();
        } catch (AssertionError e) {
            e.printStackTrace();
        }

        // bad message
        try {
            assertThrow(RuntimeException.class).withMessage("aa").whenRunning(new Code() {
                public void run() throws Throwable {
                    throw new RuntimeException("dd");
                }
            });
            fail();
        } catch (AssertionError e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test_assertThrow_containingMessgae() throws Exception {

        // ok
        assertThrow(RuntimeException.class).containingMessage("hello").whenRunning(new Code() {
            public void run() throws Throwable {
                throw new RuntimeException("hello world");
            }
        });

        // ok - null message
        assertThrow(RuntimeException.class).containingMessage(null).whenRunning(new Code() {
            public void run() throws Throwable {
                throw new RuntimeException();
            }
        });

        // no exception thrown
        try {
            assertThrow(RuntimeException.class).containingMessage("Hello !").whenRunning(new Code() {
                public void run() throws Throwable {
                }
            });
            fail();
        } catch (AssertionError e) {
            e.printStackTrace();
        }

        // message expected, got null
        try {
            assertThrow(RuntimeException.class).containingMessage("hello").whenRunning(new Code() {
                public void run() throws Throwable {
                    throw new RuntimeException();
                }
            });
            fail();
        } catch (AssertionError e) {
            e.printStackTrace();
        }

        // null expected, got message
        try {
            assertThrow(RuntimeException.class).containingMessage(null).whenRunning(new Code() {
                public void run() throws Throwable {
                    throw new RuntimeException("dd");
                }
            });
            fail();
        } catch (AssertionError e) {
            e.printStackTrace();
        }

        // bad message
        try {
            assertThrow(RuntimeException.class).containingMessage("aa").whenRunning(new Code() {
                public void run() throws Throwable {
                    throw new RuntimeException("dd");
                }
            });
            fail();
        } catch (AssertionError e) {
            e.printStackTrace();
        }
    }
    
}
