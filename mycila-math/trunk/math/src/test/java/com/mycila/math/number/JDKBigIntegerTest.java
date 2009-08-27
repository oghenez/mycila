package com.mycila.math.number;

import static com.mycila.math.number.BigInteger.*;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class JDKBigIntegerTest {

    @Test
    public void test_toRadix() {
        assertEquals(big(0).toRadix(8).toString(), "0");
        assertEquals(big(1).toRadix(8).toString(), "1");
        assertEquals(big(1000000000).toRadix(8).toString(), "7346545000");
        assertEquals(big(-1000000000).toRadix(8).toString(), "-7346545000");
    }

    @Test
    public void test_reverse() {
        assertEquals(big("123456789123456789123456789").reverseDigits(), big("987654321987654321987654321"));
        assertEquals(big(733007751850L).toRadix(2).reverseDigits().toRadix(10).toString(), "366503875925"); // 101010101010101010101010101010101010101
        assertEquals(big("ABCDEF", 16).reverseDigits().toString(), "fedcba"); // 101010101010101010101010101010101010101
    }

    @Test
    public void test_rotate() {
        assertEquals(big(1234).rotateDigits(0).toInt(), 1234);
        assertEquals(big(1234).rotateDigits(1).toInt(), 4123);
        assertEquals(big(1234).rotateDigits(2).toInt(), 3412);
        assertEquals(big(1234).rotateDigits(3).toInt(), 2341);
        assertEquals(big(1234).rotateDigits(4).toInt(), 1234);
        assertEquals(big(1234).rotateDigits(5).toInt(), 4123);
        assertEquals(big(1234).rotateDigits(-1).toInt(), 2341);
        assertEquals(big(1234).rotateDigits(-2).toInt(), 3412);
        assertEquals(big(1234).rotateDigits(-3).toInt(), 4123);
        assertEquals(big(1234).rotateDigits(-4).toInt(), 1234);
        assertEquals(big(1234).rotateDigits(-5).toInt(), 2341);
        assertEquals(big("123456789123456789123456789").rotateDigits(-5), big("678912345678912345678912345"));
    }

    @Test
    public void test_sum() {
        assertEquals(zero().digitsSum(), 0);
        assertEquals(one().digitsSum(), 1);
        assertEquals(big(9999999999L).digitsSum(), 90);
    }

    @Test
    public void test_length() {
        assertEquals(zero().length(), 1);
        assertEquals(one().length(), 1);
        assertEquals(big(9999999999L).length(), 10);
        assertEquals(big(10000).length(), 5);
    }

    @Test
    public void test_list() {
        assertArrayEquals(big(9999999999L).digits(), new int[]{9, 9, 9, 9, 9, 9, 9, 9, 9, 9});
        assertArrayEquals(big(1234567890).digits(), new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0});
        assertArrayEquals(big(9999999999L).digits(), new int[]{9, 9, 9, 9, 9, 9, 9, 9, 9, 9});
        assertArrayEquals(big(1234567890).digits(), new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0});
        long time = System.currentTimeMillis();
        for (int j = 2; j < 11; j++)
            for (long i = 10000000000L; i < 10000100000L; i++)
                big(i).digits();
        System.out.println(System.currentTimeMillis() - time);
    }

    @Test
    public void test_sort() {
        assertEquals(big(733007751850L).toRadix(2).sort().toRadix(10).toString(), "1048575"); // == 11111111111111111111
    }

    @Test
    public void test_signature() {
        assertArrayEquals(big(733007751850L).toRadix(2).digitsSignature(), new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        assertArrayEquals(big(733007751850L).digitsSignature(), new int[]{0, 0, 0, 1, 3, 3, 5, 5, 7, 7, 7, 8});
    }

    @Test
    public void test_euclide_extended() {
        BigInteger[] abc = big(123456789).euclidExtended(big(987654321));
        assertEquals(abc[0], big(-8));
        assertEquals(abc[1], big(1));
        assertEquals(abc[2], big(9));
        assertEquals(abc[2], big(123456789).gcd(big(987654321)));
        assertEquals(big(123456789).multiply(abc[0]).add(big(987654321).multiply(abc[1])), abc[2]);
    }

    @Test
    public void test_sqrtInt_big() {
        assertEquals(big(0).sqrtInt()[0], big(0));
        assertEquals(big(1).sqrtInt()[0], big(1));
        assertEquals(big(2).sqrtInt()[0], big(1));
        assertEquals(big(3).sqrtInt()[0], big(1));
        assertEquals(big(4).sqrtInt()[0], big(2));
        assertEquals(big(5).sqrtInt()[0], big(2));
        assertEquals(big(Integer.MAX_VALUE).sqrtInt()[0], big(46340));
        assertEquals(big("15241578750190521").sqrtInt()[0], big("123456789"));
        assertEquals(big("15241578750190521").sqrtInt()[1], zero());
        assertEquals(big("15241578750190530").sqrtInt()[0], big("123456789"));
        assertEquals(big("15241578750190530").sqrtInt()[1], big("9"));
        for (int i = 0; i < 1000000; i++)
            assertEquals("" + i, big(i).sqrtInt()[0], big((int) Math.sqrt(i)));
        for (int i = Integer.MAX_VALUE - 1000000; i < Integer.MAX_VALUE; i++)
            assertEquals("" + i, big(i).sqrtInt()[0], big((int) Math.sqrt(i)));
        for (long i = Long.MAX_VALUE - 1000000; i < Long.MAX_VALUE; i++)
            assertEquals("" + i, big(i).sqrtInt()[0], big((long) Math.sqrt(i)));
    }

}
