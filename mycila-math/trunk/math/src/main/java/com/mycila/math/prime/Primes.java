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

import java.util.BitSet;

/**
 * @author Mathieu Carbou
 */
public final class Primes {

    private Primes() {
    }

    /**
     * Get a high bound for pi(n), the number of
     * primes less or equal n.
     *
     * @param n Bound of the primes.
     * @return A simple estimate of the number of primes <= n.
     */
    public static int getPiHighBound(long n) {
        if (n < 17) return 6;
        return (int) Math.floor(n / (Math.log(n) - 1.5));
    }

    /**
     * Prime number sieve, Eratosthenes (276-194 B. T.).
     * <p/>
     * Algorithm from <a href="http://www.luschny.de/math/primes/PrimeSieveForJavaAndCsharp.html">Peter Luschny</a>
     *
     * @param upTo Upper bound of the sieve.
     * @return Returns the sieve computed by {@link #sieveOfEratosthenes(BitSet)}
     */
    public static BitSet sieveOfEratosthenes(int upTo) {
        final BitSet composite = new BitSet(upTo / 3);
        sieveOfEratosthenes(composite);
        return composite;
    }

    /**
     * Prime number sieve, Eratosthenes (276-194 B. T.)
     * <p/>
     * This implementation considers only multiples of primes
     * greater than 3, so the smallest value has to be mapped to 5.
     * <p/>
     * Note: There is no multiplication operation in this function.
     * <p/>
     * Algorithm from <a href="http://www.luschny.de/math/primes/PrimeSieveForJavaAndCsharp.html">Peter Luschny</a>
     *
     * @param composite After execution of the function this
     *                  BitList includes all composite numbers in [5,n]
     *                  disregarding multiples of 2 and 3.
     */
    public static void sieveOfEratosthenes(final BitSet composite) {
        int d1 = 8;
        int d2 = 8;
        int p1 = 3;
        int p2 = 7;
        int s1 = 7;
        int s2 = 3;
        int n = 0;
        int len = composite.size();
        boolean toggle = false;
        while (s1 < len) {
            if (!composite.get(n++)) {
                int inc = p1 + p2;
                for (int k = s1; k < len; k += inc)
                    composite.set(k);
                for (int k = s1 + s2; k < len; k += inc)
                    composite.set(k);
            }
            if (toggle = !toggle) {
                s1 += d2;
                d1 += 16;
                p1 += 2;
                p2 += 2;
                s2 = p2;
            } else {
                s1 += d1;
                d2 += 8;
                p1 += 2;
                p2 += 6;
                s2 = p1;
            }
        }
    }
}