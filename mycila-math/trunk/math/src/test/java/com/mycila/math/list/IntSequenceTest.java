package com.mycila.math.list;

import static org.junit.Assert.*;
import org.junit.Test;

import java.math.BigInteger;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class IntSequenceTest {

    @Test
    public void test_productBigInt() throws Exception {
        IntSequence seq = IntSequence.from(1, 2, 3, 4, 5, 6);
        assertEquals(seq.productBig(2, 4), BigInteger.valueOf(360));
        assertEquals(seq.productBig(1, 4), BigInteger.valueOf(120));
        assertEquals(seq.productBig(0, 0), BigInteger.valueOf(1));
        assertEquals(seq.productBig(1, 1), BigInteger.valueOf(2));
        assertEquals(seq.productBig(1, 2), BigInteger.valueOf(6));
        assertEquals(seq.productBig(1, 3), BigInteger.valueOf(24));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_productBigInt_err() throws Exception {
        IntSequence.from(1, 2, 3, 4, 5, 6).productBig(4, 3);
    }
}