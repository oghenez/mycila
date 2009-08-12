package com.mycila;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Mathieu Carbou
 */
public final class DivisorsTest {

    @Test
    public void test_sum() {
        assertEquals(Divisors.sum(0), 0);
        assertEquals(Divisors.sum(1), 1);
        assertEquals(Divisors.sum(11), 12);
        assertEquals(Divisors.sum(1221), 1824);
        assertEquals(Divisors.sum(3580), 7560);
        assertEquals(Divisors.sum(70), 144);
        assertEquals(Divisors.sum(8), 15);
    }

    @Test
    public void test_states() {
        assertTrue(Divisors.isPerfect(6));
        assertTrue(Divisors.isAbundant(18));
        assertTrue(Divisors.isDeficient(16));
    }

    @Test
    public void test_gcd() {
        assertEquals(Divisors.gcd(644, 645), 1);
        assertEquals(Divisors.gcd(1000, 100), 100);
        assertEquals(Divisors.gcd(15, 17), 1);
        assertEquals(Divisors.gcd(15, 18), 3);
        assertEquals(Divisors.gcd(15, 18, 27, 36, 20), 1);
        assertEquals(Divisors.gcd(15, 18, 27, 36), 3);
        assertEquals(Divisors.gcd(15, 18, 27), 3);
    }

    @Test
    public void test_lcm() {
        assertEquals(Divisors.lcm(14, 15), 210);
        assertEquals(Divisors.lcm(15, 18, 27), 270);
        assertEquals(Divisors.lcm(644, 645), 415380);
        assertEquals(Divisors.lcm(644, 646), 208012);
    }
}
