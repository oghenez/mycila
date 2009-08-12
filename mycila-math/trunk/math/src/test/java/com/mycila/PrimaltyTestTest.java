package com.mycila;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Mathieu Carbou
 */
public final class PrimaltyTestTest {

    @Test
    public void test_trialDivision() {
        assertFalse(PrimaltyTest.trialDivision(0));
        assertFalse(PrimaltyTest.trialDivision(1));
        assertTrue(PrimaltyTest.trialDivision(2));
        assertTrue(PrimaltyTest.trialDivision(7));
        assertTrue(PrimaltyTest.trialDivision(179));
        assertTrue(PrimaltyTest.trialDivision(Integer.MAX_VALUE)); // 2147483647
    }

    @Test
    public void test_millerRabin() {
        assertFalse(PrimaltyTest.millerRabin(0));
        assertFalse(PrimaltyTest.millerRabin(1));
        assertTrue(PrimaltyTest.millerRabin(2));
        assertTrue(PrimaltyTest.millerRabin(7));
        assertTrue(PrimaltyTest.millerRabin(179));
        assertTrue(PrimaltyTest.millerRabin(Integer.MAX_VALUE)); // 2147483647
    }

}