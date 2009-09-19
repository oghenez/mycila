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
package com.mycila.math;

import com.mycila.math.number.BigInt;
import static com.mycila.math.number.BigInt.*;
import com.mycila.math.prime.Primes;
import com.mycila.math.prime.Sieve;

/**
 * @author Mathieu Carbou
 */
public final class Factorial {

    private Factorial() {
    }

    // All long-representable factorials
    private static final long[] factorials = new long[]
            {1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800,
                    479001600, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L,
                    355687428096000L, 6402373705728000L, 121645100408832000L,
                    2432902008176640000L};

    /**
     * Compute the factorial of a number by using a lookup table
     *
     * @param number A positive number, between 0 and 20
     * @return The factorial
     * @throws ArithmeticException If the number is too big (> 20)
     */
    public static long lookup(int number) throws ArithmeticException {
        if (number < 0 || number >= factorials.length)
            throw new ArithmeticException("Number too big:" + number);
        return factorials[number];
    }

    //TODO: paralell prime swing http://www.luschny.de/math/factorial/java/FactorialPrimeParallelSwingLuschny.java.html

    /**
     * Compute the factorial of a number by using a lookup table if the number is low, or
     * the <a href="http://www.luschny.de/math/factorial/java/FactorialPrimeSwingLuschny.java.html">Luschny's Prime Swing algorithm</a>
     *
     * @param number A positive number
     * @return The factorial
     */
    public static BigInt primeSwingLuschny(final int number) {
        if (number <= 20) return big(lookup(number));
        final Sieve sieve = Sieve.to(number);
        final double pow2Count = Math.log(number) * 1.4426950408889634D;
        final int[] primeList = new int[(int) (2.0 * ((int) Math.sqrt(number) + number / (pow2Count - 1)))];
        int[] toStwing = new int[(int) pow2Count];
        for (int i = toStwing.length - 1, n = number; i >= 0; i--, n >>>= 1)
            toStwing[i] = n;
        BigInt recFactorial = ONE;
        for (int i = 0, max = toStwing.length; i < max; i++)
            recFactorial = recFactorial.square().multiply(swing(toStwing[i], sieve, primeList));
        return recFactorial.shiftLeft(number - Integer.bitCount(number));
    }

    private static final int[] smallOddSwing = {1, 1, 1, 3, 3, 15, 5, 35, 35, 315, 63, 693, 231, 3003, 429, 6435, 6435, 109395, 12155, 230945, 46189, 969969, 88179, 2028117, 676039, 16900975, 1300075, 35102025, 5014575, 145422675, 9694845, 300540195, 300540195};

    private static BigInt swing(final int number, Sieve sieve, int[] primeList) {
        if (number < 33) return big(smallOddSwing[number]);
        final int sqrtN = (int) Math.sqrt(number);
        final int[] pIter0 = sieve.asArray(3, sqrtN);
        final int[] pIter1 = sieve.asArray(sqrtN + 1, number / 3);
        int count = 0;
        for (int prime : pIter0) {
            int q = number, p = 1;
            while ((q /= prime) > 0) if ((q & 1) == 1) p *= prime;
            if (p > 1) primeList[count++] = p;
        }
        for (int prime : pIter1)
            if (((number / prime) & 1) == 1)
                primeList[count++] = prime;
        BigInt primorial = sieve.primorial((number >>> 1) + 1, number);
        return primorial.multiply(Primes.product(primeList, 0, count));
    }

    /**
     * Computes the <a href="http://en.wikipedia.org/wiki/Pochhammer_symbol">falling factorial</a>
     * <code>(a)n = a! / (a-n)!</code>
     *
     * @param a A positive number
     * @param n A positive number
     * @return The factorial
     */
    public static long falling(long a, long n) {
        long b = a - n;
        b++;
        long res = a > 1 ? a : 1;
        while (a-- > b) res *= a;
        return res;
    }

}
