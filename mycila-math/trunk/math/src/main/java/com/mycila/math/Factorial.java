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

import java.math.BigInteger;
import static java.math.BigInteger.*;

/**
 * @author Mathieu Carbou
 */
public final class Factorial {

    /**
     * All long-representable factorials
     */
    private static final long[] factorials = new long[]
            {1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800,
                    479001600, 6227020800l, 87178291200l, 1307674368000l, 20922789888000l,
                    355687428096000l, 6402373705728000l, 121645100408832000l,
                    2432902008176640000l};
    private static final BigInteger TWO = valueOf(2);

    private Factorial() {
    }

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

    /**
     * Compute the factorial of a number by using a lookup table if the number is low, or
     * the <a href="http://www.luschny.de/math/factorial/index.html">Split algorithm</a>
     *
     * @param number A positive number
     * @return The factorial
     */
    public static BigInteger get(int number) {
        if (number <= 20) return valueOf(lookup(number));
        BigInteger oddProduct = ONE;
        BigInteger factorialProduct = oddProduct;
        int exponentOfTwo = 0;
        for (int i = 30 - Integer.numberOfLeadingZeros(number); i >= 0; i--) {
            int m = number >>> i;
            int k = m >>> 1;
            exponentOfTwo += k;
            oddProduct = oddProduct.multiply(oddProduct(k + 1, m));
            factorialProduct = factorialProduct.multiply(oddProduct);
        }
        return factorialProduct.multiply(TWO.pow(exponentOfTwo));
    }

    private static BigInteger oddProduct(long n, long m) {
        n = n | 1;       // Round n up to the next odd number
        m = (m - 1) | 1; // Round m down to the next odd number
        if (n > m) return ONE;
        else if (n == m) return BigInteger.valueOf(n);
        else {
            long k = (n + m) >>> 1;
            return oddProduct(n, k).multiply(oddProduct(k + 1, m));
        }
    }

    //TODO: factorial for big integers: http://www.luschny.de/math/factorial/index.html + http://www.luschny.de/math/factorial/java/PrimeSieve.java.html

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

    /**
     * Computes the <a href="http://en.wikipedia.org/wiki/Pochhammer_symbol">falling factorial</a>
     * <code>(a)n = a! / (a-n)!</code>
     *
     * @param a A positive number
     * @param n A positive number
     * @return The factorial
     */
    public static BigInteger falling(BigInteger a, BigInteger n) {
        BigInteger b = a.subtract(n);
        BigInteger res = ONE;
        while (a.compareTo(b) > 0) {
            res = res.multiply(a);
            a = a.subtract(ONE);
        }
        return res;
    }

    //TODO: better multipliction algorithms: cf BigInteger.java.multiply() in TODO...
    // - Karatsuba algorithm  · Toom–Cook multiplication · Schönhage–Strassen algorithm

}
