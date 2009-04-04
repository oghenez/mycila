package com.mycila.testing.util;

import static com.mycila.testing.util.ExtendedAssert.*;
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
    public void test_assertThrow() throws Exception {
        assertThrow(RuntimeException.class).withMessage("hello").whenRunning(new Code() {
            public void run() throws Throwable {
                throw new RuntimeException("hello");
            }
        });

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

        try {
            assertThrow(RuntimeException.class).withMessage("hello").whenRunning(new Code() {
                public void run() throws Throwable {
                    throw new IllegalStateException();
                }
            });
            fail();
        } catch (AssertionError e) {
            e.printStackTrace();
        }
    }
    
}
