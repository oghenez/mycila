package com.mycila.math.number;

import static com.mycila.math.number.BigInt.*;
import static org.junit.Assert.*;
import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class JDKBigIntegerTest {

    @Test
    public void test_euclid_extended() {
        assertEquals(Arrays.toString(big(13).euclidExtended(big(168))), "[13, -1, 1]");
        assertEquals(Arrays.toString(big(17).euclidExtended(big(352))), "[145, -7, 1]");
        assertEquals(Arrays.toString(big(31).euclidExtended(big(4864))), "[-1569, 10, 1]");
        BigInt[] abc = big(123456789).euclidExtended(big(987654321));
        assertEquals(abc[0], big(-8));
        assertEquals(abc[1], big(1));
        assertEquals(abc[2], big(9));
        assertEquals(abc[2], big(123456789).gcd(big(987654321)));
        assertEquals(big(123456789).multiply(abc[0]).add(big(987654321).multiply(abc[1])), abc[2]);
    }

    @Test
    public void test_modInverse() {
        System.out.println(BigInteger.valueOf(13).modInverse(BigInteger.valueOf(168)));
        System.out.println(BigInteger.valueOf(17).modInverse(BigInteger.valueOf(352)));
        System.out.println(BigInteger.valueOf(31).modInverse(BigInteger.valueOf(4864)));
        System.out.println(BigInteger.valueOf(-45).modInverse(BigInteger.valueOf(4864)));
        System.out.println(BigInteger.valueOf(-31).modInverse(BigInteger.valueOf(3675)));
        assertEquals(big(13).modInverse(big(168)).toString(), "13");
        assertEquals(big(17).modInverse(big(352)).toString(), "145");
        assertEquals(big(31).modInverse(big(4864)).toString(), "3295");
        assertEquals(big(-31).modInverse(big(3675)).toString(), "1304");
        assertEquals(big(-45).modInverse(big(4864)).toString(), "3675");

        for (int i = 1; i < 1000;) {
            try {
                Random r = new SecureRandom();
                BigInteger x = BigInteger.valueOf(i);
                BigInteger m = new BigInteger(10, r);
                BigInteger inv = x.modInverse(m);
                BigInteger prod = inv.multiply(x).remainder(m);
                if (prod.signum() == -1) prod = prod.add(m);
                if (prod.equals(BigInteger.ONE))
                    assertEquals(wrapBig(x, 10).modInverse(wrapBig(m, 10)), wrapBig(inv, 10));
                i++;
            } catch (ArithmeticException e) {
            }
        }
    }

    @Test
    public void test_mod() {
        assertEquals(big(Long.MAX_VALUE).mod(Long.MAX_VALUE), big(0));
        assertEquals(big(Long.MAX_VALUE).add(1).mod(Long.MAX_VALUE), big(1));
        assertEquals(big(Long.MAX_VALUE).subtract(1).mod(Long.MAX_VALUE), big(9223372036854775806L));
        assertEquals(big(Long.MAX_VALUE).add(12345).mod(Long.MAX_VALUE), big(12345));
        assertEquals(big(Long.MAX_VALUE).add(Long.MAX_VALUE).mod(Long.MAX_VALUE), big(0));
        assertEquals(big(Long.MAX_VALUE).square().mod(Long.MAX_VALUE), big(0));
    }

    @Test
    public void test_gcd() {
        assertEquals(big(644).gcd(big(645)), big(1));
        assertEquals(big(1000).gcd(big(100)), big(100));
        assertEquals(big(15).gcd(big(17)), big(1));
        assertEquals(big(15).gcd(big(18)), big(3));
        assertEquals(big(15).gcd(big(18), big(27), big(36), big(20)), big(1));
        assertEquals(big(15).gcd(big(18), big(27), big(36)), big(3));
        assertEquals(big(15).gcd(big(18), big(27)), big(3));
    }

    @Test
    public void test_lcm() {
        assertEquals(big(14).lcm(big(15)), big(210));
        assertEquals(big(15).lcm(big(18), big(27)), big(270));
        assertEquals(big(644).lcm(big(645)), big(415380));
        assertEquals(big(644).lcm(big(646)), big(208012));
    }

    @Test
    public void test_toRadix() {
        assertEquals(big(0).toRadix(8).toString(), "0");
        assertEquals(big(1).toRadix(8).toString(), "1");
        assertEquals(big(1000000000).toRadix(8).toString(), "7346545000");
        assertEquals(big(-1000000000).toRadix(8).toString(), "-7346545000");
    }

    @Test
    public void test_reverse() {
        assertEquals(big("123456789123456789123456789").digitsReversed(), big("987654321987654321987654321"));
        assertEquals(big(733007751850L).toRadix(2).digitsReversed().toRadix(10).toString(), "366503875925"); // 101010101010101010101010101010101010101
        assertEquals(big("ABCDEF", 16).digitsReversed().toString(), "fedcba"); // 101010101010101010101010101010101010101
    }

    @Test
    public void test_rotate() {
        assertEquals(big(1234).digitsRotated(0).toInt(), 1234);
        assertEquals(big(1234).digitsRotated(1).toInt(), 4123);
        assertEquals(big(1234).digitsRotated(2).toInt(), 3412);
        assertEquals(big(1234).digitsRotated(3).toInt(), 2341);
        assertEquals(big(1234).digitsRotated(4).toInt(), 1234);
        assertEquals(big(1234).digitsRotated(5).toInt(), 4123);
        assertEquals(big(1234).digitsRotated(-1).toInt(), 2341);
        assertEquals(big(1234).digitsRotated(-2).toInt(), 3412);
        assertEquals(big(1234).digitsRotated(-3).toInt(), 4123);
        assertEquals(big(1234).digitsRotated(-4).toInt(), 1234);
        assertEquals(big(1234).digitsRotated(-5).toInt(), 2341);
        assertEquals(big("123456789123456789123456789").digitsRotated(-5), big("678912345678912345678912345"));
    }

    @Test
    public void test_sum() {
        assertEquals(ZERO.digitsSum(), 0);
        assertEquals(ONE.digitsSum(), 1);
        assertEquals(big(9999999999L).digitsSum(), 90);
    }

    @Test
    public void test_length() {
        assertEquals(ZERO.digitsCount(), 1);
        assertEquals(ONE.digitsCount(), 1);
        assertEquals(big(9999999999L).digitsCount(), 10);
        assertEquals(big(10000).digitsCount(), 5);
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
    public void test_digitalRoot() {
        for (int i = 0; i < 10; i++)
            assertEquals(big(i).digitalRoot(), i);
        assertEquals(big(65536).digitalRoot(), 7);
        assertEquals(big(0).digitalRoot(), 0);
        assertEquals(big(Long.MAX_VALUE).digitalRoot(), 7);
    }

    @Test
    public void test_signature() {
        assertArrayEquals(big(733007751850L).toRadix(2).digitsSignature(), new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        assertArrayEquals(big(733007751850L).digitsSignature(), new int[]{0, 0, 0, 1, 3, 3, 5, 5, 7, 7, 7, 8});
    }

    @Test
    public void test_root() {
        assertArrayEquals(big(14).irootAndRemainder(5), new BigInt[]{big(1), big(13)});

        assertArrayEquals(big(Integer.MAX_VALUE).irootAndRemainder(4), new BigInt[]{big(215), big(10733022)});
        assertArrayEquals(big("15241578750190521").irootAndRemainder(2), new BigInt[]{big("123456789"), big(0)});
        assertArrayEquals(big("15241578750190530").irootAndRemainder(2), new BigInt[]{big("123456789"), big(9)});
        assertArrayEquals(big("1881676371789154860897089").irootAndRemainder(3), new BigInt[]{big("123456789"), big(20)});

        assertArrayEquals(big(0).irootAndRemainder(2), new BigInt[]{big(0), big(0)});
        assertArrayEquals(big(1).irootAndRemainder(2), new BigInt[]{big(1), big(0)});
        assertArrayEquals(big(2).irootAndRemainder(2), new BigInt[]{big(1), big(1)});
        assertArrayEquals(big(3).irootAndRemainder(2), new BigInt[]{big(1), big(2)});
        assertArrayEquals(big(3).irootAndRemainder(3), new BigInt[]{big(1), big(2)});
        assertArrayEquals(big(4).irootAndRemainder(2), new BigInt[]{big(2), big(0)});
        assertArrayEquals(big(5).irootAndRemainder(2), new BigInt[]{big(2), big(1)});

        Random m = new Random();
        for (int i = 1; i < 10000; i++) {
            int root = m.nextInt(10) + 1;
            BigInt[] qr = big(i).irootAndRemainder(root);
            assertEquals("" + i, big(i), qr[0].pow(root).add(qr[1]));
        }
    }

    @Test
    public void test_sqrt() {
        assertArrayEquals(big(0).isqrtAndRemainder(), new BigInt[]{big(0), big(0)});
        assertArrayEquals(big(1).isqrtAndRemainder(), new BigInt[]{big(1), big(0)});
        assertArrayEquals(big(2).isqrtAndRemainder(), new BigInt[]{big(1), big(1)});
        assertArrayEquals(big(3).isqrtAndRemainder(), new BigInt[]{big(1), big(2)});
        assertArrayEquals(big("15241578750190521").irootAndRemainder(2), new BigInt[]{big("123456789"), big(0)});
        assertArrayEquals(big("15241578750190530").irootAndRemainder(2), new BigInt[]{big("123456789"), big(9)});
        assertArrayEquals(big(Integer.MAX_VALUE).irootAndRemainder(2), new BigInt[]{big(46340), big(88047)});

        for (int i = 1; i < 10000; i++) {
            BigInt[] qr = big(i).isqrtAndRemainder();
            assertEquals("" + i, big(i), qr[0].square().add(qr[1]));
        }
        for (int i = Integer.MAX_VALUE - 100000; i < Integer.MAX_VALUE; i++) {
            BigInt[] qr = big(i).isqrtAndRemainder();
            assertEquals("" + i, big(i), qr[0].square().add(qr[1]));
        }
        for (long i = Long.MAX_VALUE - 100000; i < Long.MAX_VALUE; i++) {
            BigInt[] qr = big(i).isqrtAndRemainder();
            assertEquals("" + i, big(i), qr[0].square().add(qr[1]));
        }
    }

    @Test
    public void test_millerRabin() {
        assertEquals(ZERO.isPrimeMillerRabin(), 0.0, 0.000000001);
        assertEquals(ONE.isPrimeMillerRabin(), 0.0, 0.000000001);
        assertEquals(TWO.isPrimeMillerRabin(), 1.0, 0.000000001);
        assertEquals(big(7).isPrimeMillerRabin(), 1.0, 0.000000001);
        assertEquals(big(179).isPrimeMillerRabin(), 1.0, 0.000000001);
        //assertEquals(big(Integer.MAX_VALUE).isPrimeMillerRabin(), 1.0, 0.000000001);
        //assertEquals(big((long) Integer.MAX_VALUE + 1L).isPrimeMillerRabin(), 1.0, 0.000000001);
    }

    @Test
    public void test_lucasLehmer() {
        assertFalse(ZERO.isPrimeLucasLehmer());
        assertFalse(big(4).isPrimeLucasLehmer());
        assertTrue(TWO.isPrimeLucasLehmer());
        assertTrue(big(3).isPrimeLucasLehmer());
        assertTrue(big(5).isPrimeLucasLehmer());
        assertTrue(big(31).isPrimeLucasLehmer()); // matches 2^31-1 = Integer.MAX_VALUE
    }

}
