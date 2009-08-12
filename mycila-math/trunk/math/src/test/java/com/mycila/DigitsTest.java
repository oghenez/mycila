package com.mycila;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Mathieu Carbou
 */
public final class DigitsTest {

    @Test
    public void test_reverse() {
        assertEquals("reverse(0)", Digits.base(10).reverse(0), 0);
        assertEquals("reverse(1230)", Digits.base(10).reverse(1230), 321);
        assertEquals("reverse(1000)", Digits.base(10).reverse(1000), 1);
        assertEquals("reverse(9999)", Digits.base(10).reverse(9999), 9999);
    }

}