package com.mycila.testing.plugin.db;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ClassUtilsTest {
    @Test
    public void test_isNumber() throws Exception {
        Object o = Byte.valueOf("1");
        System.out.println(o.getClass().getName());
        assertTrue(ClassUtils.isNumber(o));
        o = (byte) 1;
        System.out.println(o.getClass().getName());
        assertTrue(ClassUtils.isNumber(o));
    }
}
