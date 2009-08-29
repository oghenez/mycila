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
package com.mycila.math.prime;

import com.mycila.math.Mod;
import com.mycila.math.number.BigInt;

/**
 * @author Mathieu Carbou
 */
public final class PrimaltyTest {

    private static final BigInt TWO = BigInt.big(2);

    private PrimaltyTest() {
    }

    /**
     * Check if the number is prime against a list of prime.
     *
     * @param number   The number to check
     * @param primes   The prime list, in ascending order
     * @param maxIndex The maximum index to go in the list, to reduce search if needed
     * @return True if the number is prime against this prime list
     */
    public static boolean isPrime(int number, int[] primes, int maxIndex) {
        final int sqrtFloor = (int) Math.sqrt(number);
        for (int i = 0; i < maxIndex; i++) {
            final int prime = primes[i];
            if (prime > sqrtFloor) return true;
            if (number % prime == 0) return false;
        }
        return true;
    }

    /**
     * Primalty test using <a href="http://en.wikipedia.org/wiki/Trial_division">Trial Division<a/>.
     *
     * @param number The number to test
     * @return True if it is prime
     */
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

    /**
     * Primalty test using <a href="http://en.wikipedia.org/wiki/Trial_division">Trial Division<a/>.
     *
     * @param number The number to test
     * @return True if it is prime
     */
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

    /**
     * Primalty test using <a href="http://en.wikipedia.org/wiki/Miller%E2%80%93Rabin_primality_test">Miller-Rabin primality test<a/>.
     * <p/>
     * Using <a href="http://en.literateprograms.org/Miller-Rabin_primality_test_(Java)">this implementation</a> to cover all possible primes in 32bits.
     *
     * @param number The number to test
     * @return True if it is prime
     */
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
        d >>>= s;
        int a_to_power = Mod.pow(a, d, n);
        s--;
        if (a_to_power == 1) return true;
        for (int i = 0; i < s; i++) {
            if (a_to_power == n - 1) return true;
            a_to_power = Mod.pow(a_to_power, 2, n);
        }
        return a_to_power == n - 1;
    }

    //TODO: http://en.wikipedia.org/wiki/AKS_primality_test + ZIP AKS
    //TODO: http://en.wikipedia.org/wiki/Adleman%E2%80%93Pomerance%E2%80%93Rumely_primality_test + ECM pour APR-CL
    //TODO: http://en.wikipedia.org/wiki/Elliptic_curve_primality_proving + ECM applet
    //TODO: Milelr-Rabin in BigIntegerMath

    /**
     * Primalty test using <a href="http://en.wikipedia.org/wiki/Miller%E2%80%93Rabin_primality_test">Miller-Rabin primality test<a/>.
     * <p/>
     * Using <a href="http://en.literateprograms.org/Miller-Rabin_primality_test_(Java)">this implementation</a>.
     * <p/>
     * The number of pass has been set to 50.
     *
     * @param number The number to test
     * @return True if it is prime
     */
    public static boolean millerRabin(BigInt number) {
        for (int repeat = 0; repeat < 50; repeat++) {
            BigInt a;
            do a = BigInt.random(number.bitLength());
            while (a.signum() == 0);
            if (!millerRabinPass(a, number)) return false;
        }
        return true;
    }

    private static boolean millerRabinPass(BigInt a, BigInt n) {
        final BigInt n_minus_one = n.subtract(BigInt.one());
        final int s = n_minus_one.lowestSetBit();
        BigInt pow = a.modPow(n_minus_one.shiftRight(s), n);
        if (pow.equals(BigInt.one())) return true;
        for (int i = 0; i < s - 1; i++) {
            if (pow.equals(n_minus_one)) return true;
            pow = pow.multiply(pow).mod(n);
        }
        return pow.equals(n_minus_one);
    }

    /**
     * Primalty test using <a href="http://en.wikipedia.org/wiki/Lucas%E2%80%93Lehmer_test_for_Mersenne_numbers">Lucas-Lehmer primality test<a/>
     * for <a href="http://en.wikipedia.org/wiki/Mersenne_prime">Mersenne primes</a>.
     * <p/>
     * Determine if Mp = 2^p - 1 is prime with p an odd prime
     * <p/>
     * Using <a href="http://en.literateprograms.org/Lucas-Lehmer_test_for_Mersenne_numbers_(Java)">this implementation</a>.
     * <p/>
     * The number of pass has been set to 50.
     *
     * @param p The number to test
     * @return True if it is prime
     */
    public static boolean lucasLehmer(int p) {
        if (p == 2) return true;
        final BigInt m = TWO.pow(p).subtract(BigInt.one());
        BigInt s = BigInt.big(4);
        for (int i = 0; i < p - 2; i++)
            s = s.multiply(s).subtract(TWO).mod(m);
        return s.equals(BigInt.zero());
    }

}
