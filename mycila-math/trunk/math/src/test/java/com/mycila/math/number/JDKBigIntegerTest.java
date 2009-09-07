/**
 * Copyright (C) 2009 Mathieu Carbou <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mycila.math.number;

import static com.mycila.math.number.BigInt.*;
import com.mycila.math.number.jdk7.BigInteger;
import com.mycila.math.prime.Sieve;
import static org.junit.Assert.*;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class JDKBigIntegerTest {

    @Test
    public void test_isEulerJacobiPseudoprime() {
        assertTrue(big(561).isPseudoprimeEulerJacobi(big(2)));
        assertTrue(big(2465).isPseudoprimeEulerJacobi(big(61)));
        assertTrue(big(1309).isPseudoprimeEulerJacobi(big(67)));
        assertFalse(big(1309).isPseudoprimeEulerJacobi(big(61)));
    }

    @Test
    public void test_isPrimeSolovayStrassen() {
        assertFalse(ZERO.isPrimeSolovayStrassen());
        assertFalse(ONE.isPrimeSolovayStrassen());
        assertTrue(TWO.isPrimeSolovayStrassen());
        assertTrue(big(7).isPrimeSolovayStrassen());
        assertTrue(big(179).isPrimeSolovayStrassen());
        assertTrue(big(Integer.MAX_VALUE).isPrimeSolovayStrassen());
        assertTrue(big(Integer.MAX_VALUE).nextPrime().nextPrime().isPrimeSolovayStrassen());
        // Test some Carmichael numbers
        assertFalse(big(561).isPrimeSolovayStrassen());
        assertFalse(big(1105).isPrimeSolovayStrassen());
        assertFalse(big(1729).isPrimeSolovayStrassen());
        assertFalse(big(2100901).isPrimeSolovayStrassen());
        for (int p : Sieve.to(10000).asArray())
            assertTrue(big(p).isPrimeSolovayStrassen());
    }

    @Test
    public void test_isPrimeFermatLittle() {
        assertFalse(ZERO.isPrimeFermatLittle());
        assertFalse(ONE.isPrimeFermatLittle());
        assertTrue(TWO.isPrimeFermatLittle());
        assertTrue(big(7).isPrimeFermatLittle());
        assertTrue(big(179).isPrimeFermatLittle());
        assertTrue(big(Integer.MAX_VALUE).isPrimeFermatLittle());
        assertTrue(big(Integer.MAX_VALUE).nextPrime().nextPrime().isPrimeFermatLittle());
        // Test some Carmichael numbers 
        assertFalse(big(561).isPrimeFermatLittle());
        assertFalse(big(1105).isPrimeFermatLittle());
        assertFalse(big(1729).isPrimeFermatLittle());
        assertFalse(big(2100901).isPrimeFermatLittle());
    }

    @Test
    public void test_isPandigital() {
        assertTrue(big(932718654).isPandigital(1, 9));
        assertTrue(big(9327186540L).isPandigital());
        assertTrue(big(93271888654000L).isPandigital());
        assertFalse(big(33).isPandigital(1, 9));
        assertTrue(big(123456789).isPandigital(1, 9));
        assertTrue(big(946138257).isPandigital(1, 9));
        assertTrue(big(1234).isPandigital(1, 4));
        assertFalse(big(1244).isPandigital(1, 4));
        assertFalse(big(946108257).isPandigital(1, 9));
        assertTrue(big(1234567899).isPandigital(1, 9));
        assertFalse(big(1234567899).isPandigital());
        assertFalse(big(100).isPandigital(1, 2));
        assertTrue(big(100).isPandigital(0, 1));
        assertTrue(big(100).toRadix(2).isPandigital());
        assertTrue(big(120).isPandigital(0, 2));
        assertTrue(big(342).isPandigital(2, 4));
    }

    @Test
    public void test_range() {
        assertArrayEquals(big(0).pandigitalRange(), new int[]{0, 0});
        assertArrayEquals(big(1).pandigitalRange(), new int[]{1, 1});
        assertArrayEquals(big(57603421).pandigitalRange(), new int[]{0, 7});
        assertArrayEquals(big(5763421).pandigitalRange(), new int[]{1, 7});
        assertArrayEquals(big(10).pandigitalRange(), new int[]{0, 1});
        assertArrayEquals(big(23459).pandigitalRange(), null);
        assertArrayEquals(big(9876543210L).pandigitalRange(), new int[]{0, 9});
    }

    @Test
    public void test_jacobiSymbol() throws Exception {
        Random r = new SecureRandom();

        assertEquals(BigInteger.jacobiSymbol(79, BigInteger.valueOf(3)), 1);
        assertEquals(big(3).jacobiSymbol(big(79)), ONE);

        assertEquals(BigInteger.jacobiSymbol(5, BigInteger.valueOf(7)), -1);
        assertEquals(big(7).jacobiSymbol(big(5)), ONE.opposite());

        assertEquals(BigInteger.jacobiSymbol(81, BigInteger.valueOf(7)), 1);
        assertEquals(big(7).jacobiSymbol(big(81)), ONE);

        assertEquals(BigInteger.jacobiSymbol(691385, BigInteger.valueOf(9)), 1);
        assertEquals(big(9).jacobiSymbol(big(691385)), ONE);

        assertEquals(0, BigInteger.jacobiSymbol(26970, BigInteger.valueOf(9)));
        assertEquals(ZERO, big(9).jacobiSymbol(big(26970)));

        for (int i = 3; i < 100000; i += 2) {
            int a = r.nextInt(1000001);
            assertEquals("(" + a + "/" + i + ")",
                    big(BigInteger.jacobiSymbol(a, BigInteger.valueOf(i))),
                    big(i).jacobiSymbol(big(a)));
        }
    }

    @Test
    public void test_falling() {
        assertEquals(big(0).fallingFactorial(0), big(1));
        assertEquals(big(1).fallingFactorial(1), big(1));
        assertEquals(big(2).fallingFactorial(1), big(2));
        assertEquals(big(20).fallingFactorial(10), big(670442572800L));
    }

    @Test
    public void sumTo() {
        assertEquals(big(6).sumTo(3), big(3).sumTo(6));
        assertEquals(big(6).sumTo(3), big(18));
        assertEquals(big(1).sumTo(10), big(55));
    }

    @Test
    public void productTo() {
        assertEquals(big(6).productTo(3), big(3).productTo(6));
        assertEquals(big(6).productTo(3), big(360));
        assertEquals(big(1).productTo(10), big(3628800));
    }

    @Test
    public void recurringCycle() {
        assertArrayEquals(TWO.recuringCycle(), new BigInt[]{ZERO, ZERO});
        assertArrayEquals(big(983).recuringCycle(), new BigInt[]{big("0010172939979654120040691759918616480162767039674465920651068158697863682604272634791454730417090539165818921668362156663275686673448626653102746693794506612410986775178026449643947100712105798575788402848423194303153611393692777212614445574771108850457782299084435401831129196337741607324516785350966429298067141403865717192268565615462868769074262461851475076297049847405900305188199389623601220752797558494404883011190233977619532044760935910478128179043743641912512716174974567650050864699898270600203458799593082400813835198372329603255340793489318413021363173957273652085452695829094608341810783316378433367243133265513733468972533062054933875890132248219735503560528992878942014242115971515768056968463886063072227873855544252288911495422177009155645981688708036622583926754832146490335707019328585961342828077314343845371312309257375381485249237029501525940996948118006103763987792472024415055951169888097660223804679552390640895218718209562563580874872838250254323499491353"), big(982)});
        assertArrayEquals(big(5).recuringCycle(), new BigInt[]{ZERO, ZERO});
        assertArrayEquals(big(3).recuringCycle(), new BigInt[]{big(3), ONE});
    }

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

        Random r = new SecureRandom();
        for (int i = 1; i < 1000;) {
            try {
                BigInteger x = BigInteger.valueOf(i);
                BigInteger m = new BigInteger(10, r);
                BigInteger inv = x.modInverse(m);
                BigInteger prod = inv.multiply(x).remainder(m);
                if (prod.signum() == -1) prod = prod.add(m);
                if (prod.equals(BigInteger.ONE))
                    assertEquals(big(x.toString()).modInverse(big(m.toString())), big(inv.toString()));
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
        /*assertEquals(big(1234).digitsRotated(0).toInt(), 1234);
        assertEquals(big(1234).digitsRotated(1).toInt(), 4123);
        assertEquals(big(1234).digitsRotated(2).toInt(), 3412);
        assertEquals(big(1234).digitsRotated(3).toInt(), 2341);
        assertEquals(big(1234).digitsRotated(4).toInt(), 1234);
        assertEquals(big(1234).digitsRotated(5).toInt(), 4123);*/
        assertEquals(big(123456).digitsRotated(3).toInt(), 456123);
        assertEquals(big(123456).digitsRotated(2).toInt(), 561234);
        assertEquals(big(1234567).digitsRotated(3).toInt(), 5671234);
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
        assertArrayEquals(big(9999999999L).digits(), new byte[]{9, 9, 9, 9, 9, 9, 9, 9, 9, 9});
        assertArrayEquals(big(1234567890).digits(), new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0});
        assertArrayEquals(big(9999999999L).digits(), new byte[]{9, 9, 9, 9, 9, 9, 9, 9, 9, 9});
        assertArrayEquals(big(1234567890).digits(), new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0});
        long time = System.currentTimeMillis();
        for (int j = 2; j < 11; j++)
            for (long i = 10000000000L; i < 10000100000L; i++)
                big(i).digits();
        System.out.println(System.currentTimeMillis() - time);
    }

    @Test
    public void test_sort() {
        assertEquals(big(733007751850L).toRadix(2).digitsSorted().toRadix(10).toString(), "1048575"); // == 11111111111111111111
        assertEquals(big(1234567890L).digitsSorted().toString(), "123456789");
        assertEquals(big(9876543210L).digitsSorted().toString(), "123456789");
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
        assertArrayEquals(big(733007751850L).toRadix(2).digitsSignature(), new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1});
        assertArrayEquals(big(733007751850L).digitsSignature(), new byte[]{0, 0, 0, 1, 3, 3, 5, 5, 7, 7, 7, 8});
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
        assertFalse(ZERO.isPrimeMillerRabin());
        assertFalse(ONE.isPrimeMillerRabin());
        assertTrue(TWO.isPrimeMillerRabin());
        assertTrue(big(7).isPrimeMillerRabin());
        assertTrue(big(179).isPrimeMillerRabin());
        System.out.println(BigInteger.valueOf(Integer.MAX_VALUE).isProbablePrime(100));
        assertTrue(big(Integer.MAX_VALUE).isPrimeMillerRabin());
        assertTrue(big(Integer.MAX_VALUE).nextPrime().nextPrime().isPrimeMillerRabin());
    }

    @Test
    public void test_isPrimeLucasLehmer() {
        assertFalse(ZERO.isPrimeLucasLehmer());
        assertFalse(big(4).isPrimeLucasLehmer());
        assertTrue(TWO.isPrimeLucasLehmer());
        assertTrue(big(3).isPrimeLucasLehmer());
        assertTrue(big(5).isPrimeLucasLehmer());
        assertTrue(big(31).isPrimeLucasLehmer()); // matches 2^31-1 = Integer.MAX_VALUE
        assertTrue(big(Integer.MAX_VALUE).isPrimeLucasLehmer()); // matches 2^31-1 = Integer.MAX_VALUE
        assertTrue(big(Integer.MAX_VALUE).nextPrime().nextPrime().isPrimeLucasLehmer());
        assertTrue(big(Integer.MAX_VALUE).square().nextPrime().nextPrime().isPrimeLucasLehmer());
        for (int p : Sieve.to(10000).asArray())
            assertTrue(big(p).isPrimeLucasLehmer());
    }

    @Test
    public void test_isPrimeMersenne() {
        assertFalse(ZERO.isPrimeMersenne());
        assertFalse(big(4).isPrimeMersenne());
        assertTrue(TWO.isPrimeMersenne());
        assertTrue(big(3).isPrimeMersenne());
        assertTrue(big(5).isPrimeMersenne());
        assertTrue(big(31).isPrimeMersenne()); // matches 2^31-1 = Integer.MAX_VALUE
        assertFalse(big(63).isPrimeMersenne()); // matches 2^63-1 = Long.MAX_VALUE
        assertFalse(big(1023).isPrimeMersenne());
    }

}
