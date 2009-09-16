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

import com.mycila.math.concurrent.ConcurrentOperations;
import com.mycila.math.concurrent.MultiplyOperation;
import com.mycila.math.number.BigInt;
import static com.mycila.math.number.BigInt.*;

import java.util.BitSet;

/**
 * @author Mathieu Carbou
 */
public final class Primes {

    private Primes() {
    }

    public static BigInt product(int[] numbers, int offset, int length) {
        if (length == 0) return ONE;
        if (length == 1) return big(numbers[offset]);
        if (length == 2) return big((long) numbers[offset] * (long) numbers[offset + 1]);
        long[] res = new long[(length >>> 1) + (length & 1)];
        int pos = 0;
        for (int i = offset, max = offset + length; i < max;) {
            res[pos] = numbers[i++];
            // 4294967298 = Long.MAX_VALUE / Integer.MAX_VALUE = 223372036854775807 / 2147483647;
            while (i < max && res[pos] <= 4294967298L)
                res[pos] *= numbers[i++];
            pos++;
        }
        if (pos == 1)
            return big(res[0]);
        if (pos == 2)
            return big(res[0]).multiply(big(res[1]));
        MultiplyOperation multiply = ConcurrentOperations.multiply();
        for (pos--; pos > 0; pos -= 2)
            multiply.multiply(big(res[pos]), big(res[pos - 1]));
        while (multiply.size() > 1)
            multiply.multiply(multiply.take(), multiply.take());
        return pos == 0 ? big(res[0]).multiply(multiply.take()) : multiply.take();
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