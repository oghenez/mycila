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
package com.mycila;

import com.mycila.math.Mod;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author Mathieu Carbou
 */
public final class PrimaltyTest {

    private static final BigInteger TWO = BigInteger.valueOf(2);
    private static final Random RANDOM = new Random();

    private PrimaltyTest() {
    }

    public static boolean trialDivision(int number) {
        if (number == 2 || number == 3) return true;
        if (number < 2 || (number & 1) == 0) return false;
        if (number < 9) return true;
        if (number % 3 == 0) return false;
        int r = (int) Math.sqrt(number);
        int f = 5;
        while (f <= r) {
            if (number % f == 0 || number % (f + 2) == 0)
                return false;
            f += 6;
        }
        return true;
    }

    public static boolean trialDivision(long number) {
        if (number == 2 || number == 3) return true;
        if (number < 2 || (number & 1) == 0) return false;
        if (number < 9) return true;
        if (number % 3 == 0) return false;
        long r = (long) Math.sqrt(number);
        long f = 5;
        while (f <= r) {
            if (number % f == 0 || number % (f + 2) == 0)
                return false;
            f += 6;
        }
        return true;
    }

    public static boolean millerRabin(int number) {
        return number > 1
                && (number == 2
                || millerRabinPass(2, number)
                && (number <= 7 || millerRabinPass(7, number))
                && (number <= 61 || millerRabinPass(61, number)));
    }

    private static boolean millerRabinPass(final int a, final int n) {
        int d = n - 1;
        int s = Integer.numberOfTrailingZeros(d);
        d >>= s;
        int a_to_power = Mod.pow(a, d, n);
        s--;
        if (a_to_power == 1) return true;
        for (int i = 0; i < s; i++) {
            if (a_to_power == n - 1) return true;
            a_to_power = Mod.pow(a_to_power, 2, n);
        }
        return a_to_power == n - 1;
    }

    public static boolean millerRabin(BigInteger n) {
        for (int repeat = 0; repeat < 50; repeat++) {
            BigInteger a;
            do a = new BigInteger(n.bitLength(), RANDOM);
            while (a.signum() == 0);
            if (!millerRabinPass(a, n)) return false;
        }
        return true;
    }

    private static boolean millerRabinPass(BigInteger a, BigInteger n) {
        final BigInteger n_minus_one = n.subtract(BigInteger.ONE);
        final int s = n_minus_one.getLowestSetBit();
        BigInteger pow = a.modPow(n_minus_one.shiftRight(s), n);
        if (pow.equals(BigInteger.ONE)) return true;
        for (int i = 0; i < s - 1; i++) {
            if (pow.equals(n_minus_one)) return true;
            pow = pow.multiply(pow).mod(n);
        }
        return pow.equals(n_minus_one);
    }

    // http://en.wikipedia.org/wiki/Lucas%E2%80%93Lehmer_test_for_Mersenne_numbers
    // Determine if Mp = 2p ? 1 is prime with p an odd prime
    public static boolean lucasLehmer(int p) {
        if (p == 2) return true;
        final BigInteger m = BigInteger.valueOf(2).pow(p).subtract(BigInteger.ONE);
        BigInteger s = BigInteger.valueOf(4);
        for (int i = 0; i < p - 2; i++)
            s = s.multiply(s).subtract(TWO).mod(m);
        return s.equals(BigInteger.ZERO);
    }

}
