package com.mycila;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Mathieu Carbou
 */
public final class ModTest {

    @Test
    public void test_add() {
        assertEquals(Mod.add(131, 145, 100), 76);
        assertEquals(Mod.add(Integer.MAX_VALUE, Integer.MAX_VALUE, 100), 94);
    }

    @Test
    public void test_mult() {
        assertEquals(Mod.multiply(131, 145, 100), 95);
        assertEquals(Mod.multiply(Integer.MAX_VALUE, Integer.MAX_VALUE, 100), 9);
    }

    @Test
    public void test_pow() {
        assertEquals(Mod.pow(131, 3, 100), 91);
        assertEquals(Mod.pow(Integer.MAX_VALUE, 2, 100), 9);
    }

}