package com.mycila;

import static org.junit.Assert.*;
import org.junit.Test;

import java.math.BigInteger;

/**
 * @author Mathieu Carbou
 */
public final class FibonacciTest {

    private static final long[] expected = {0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144, 233, 377, 610, 987, 1597, 2584, 4181, 6765, 10946, 17711, 28657, 46368, 75025};

    @Test
    public void test_isFibonaci() {
        for (int i = 0, max = expected.length; i < max; i++) {
            assertTrue(Fibonacci.isFibonaci(expected[i]));
        }
    }

    @Test
    public void test_gen() {
        for (int i = 0, max = expected.length; i < max; i++) {
            assertEquals(Fibonacci.binet(i), expected[i]);
            assertEquals(Fibonacci.iterative(i), BigInteger.valueOf(expected[i]));
            assertEquals(Fibonacci.logarithmic(i), BigInteger.valueOf(expected[i]));
        }
    }

}