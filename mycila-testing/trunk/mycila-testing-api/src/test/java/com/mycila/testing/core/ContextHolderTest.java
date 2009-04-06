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
