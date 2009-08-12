package com.mycila;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Mathieu Carbou
 */
public final class PandigitalTest {

    @Test
    public void test_isPandigital() {
        Pandigital pandigital = Pandigital.base(10);
        assertFalse(pandigital.isPandigital(33, 1, 9));
        assertTrue(pandigital.isPandigital(123456789, 1, 9));
        assertTrue(pandigital.isPandigital(946138257, 1, 9));
        assertTrue(pandigital.isPandigital(1234, 1, 4));
        assertFalse(pandigital.isPandigital(1244, 1, 4));
        assertFalse(pandigital.isPandigital(946108257, 1, 9));
        assertFalse(pandigital.isPandigital(1234567899, 1, 9));
        assertFalse(pandigital.isPandigital(100, 1, 2));
        assertFalse(pandigital.isPandigital(100, 0, 1));
        assertTrue(pandigital.isPandigital(120, 0, 2));
        assertTrue(pandigital.isPandigital(342, 2, 4));
    }

    @Test
    public void test_range() {
        Pandigital pandigital = Pandigital.base(10);
        assertEquals(pandigital.range(0), Range.range(0, 0));
        assertEquals(pandigital.range(1), Range.range(1, 1));
        assertEquals(pandigital.range(57603421), Range.range(0, 7));
        assertEquals(pandigital.range(5763421), Range.range(1, 7));
        assertEquals(pandigital.range(10), Range.range(0, 1));
        assertEquals(pandigital.range(23459), null);
        assertEquals(pandigital.range(9876543210L), Range.range(0, 9));

    }

}
