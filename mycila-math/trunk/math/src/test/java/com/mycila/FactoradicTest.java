package com.mycila;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author Mathieu Carbou
 */
public final class FactoradicTest {

    @Test
    public void test_digits() {
        assertEquals(Factoradic.base(10).toString(), "Factoradic base 10: [362880, 40320, 5040, 720, 120, 24, 6, 2, 1, 1]");
        assertArrayEquals(Factoradic.base(10).digits(999999), new int[]{2, 6, 6, 2, 5, 1, 2, 1, 1, 0});
        assertArrayEquals(Factoradic.base(10).digits(145), new int[]{0, 0, 0, 0, 1, 1, 0, 0, 1, 0});
        assertArrayEquals(Factoradic.base(10).digits(154), new int[]{0, 0, 0, 0, 1, 1, 1, 2, 0, 0});
        assertArrayEquals(Factoradic.base(10).digits(541), new int[]{0, 0, 0, 0, 4, 2, 2, 0, 1, 0});
        assertArrayEquals(Factoradic.base(10).digits(121), new int[]{0, 0, 0, 0, 1, 0, 0, 0, 1, 0});
        assertArrayEquals(Factoradic.base(10).digits(123), new int[]{0, 0, 0, 0, 1, 0, 0, 1, 1, 0});
    }

    @Test
    public void test_permutation() {
        assertEquals(Factoradic.base(10).permutation(999999, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9), Arrays.asList(2, 7, 8, 3, 9, 1, 5, 4, 6, 0));
        assertEquals(Factoradic.base(10).permutation(0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9), Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));
        assertEquals(Factoradic.base(10).permutation(3628799, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9), Arrays.asList(9, 8, 7, 6, 5, 4, 3, 2, 1, 0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_permutation2() {
        Factoradic.base(10).permutation(3628800, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    }
}
