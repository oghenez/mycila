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
    private static final BigInteger TWENTY = valueOf(20);
    private static final BigInteger TWO = valueOf(2);

    private Factorial() {
    }

    /**
     * Compute the factorial of a number.
     * <p/>
     * <b>Implementation:</b>
     * <p/>
     * Uses the trivial algorithm
     *
     * @param number A positive number
     * @return The factorial
     */
    public static long get(int number) {
        if (number < 0 || number >= factorials.length)
            throw new ArithmeticException("Number too big:" + number);
        return factorials[number];
    }

    /**
     * Compute the factorial of a number.
     * <p/>
     * <b>Implementation:</b>
     * <p/>
     * Uses the trivial algorithm
     *
     * @param n A positive number
     * @return The factorial
     */
    public static BigInteger trivial(BigInteger n) {
        if (n.signum() < 0)
            throw new ArithmeticException("Invalid number:" + n);
        if (n.compareTo(TWENTY) <= 0)
            return valueOf(factorials[n.intValue()]);
        BigInteger f = ONE;
        while (n.signum() > 0) {
            f = f.multiply(n);
            n = n.subtract(ONE);
        }
        return f;
    }

    /**
     * Computes <code>a! / b! for a > b</code>
     * <p/>
     * <b>Implementation:</b>
     * <p/>
     * Uses the trivial algorithm
     *
     * @param a A positive number
     * @param b A positive number
     * @return The factorial
     */
    public static long trivialDiv(long a, long b) {
        b++;
        long res = a > 1 ? a : 1;
        while (a-- > b) res *= a;
        return res;
    }

    /**
     * Computes <code>a! / b! for a > b</code>
     * <p/>
     * <b>Implementation:</b>
     * <p/>
     * Uses the trivial algorithm
     *
     * @param a A positive number
     * @param b A positive number
     * @return The factorial
     */
    public static BigInteger trivialDiv(BigInteger a, BigInteger b) {
        BigInteger res = ONE;
        while (a.compareTo(b) > 0) {
            res = res.multiply(a);
            a = a.subtract(ONE);
        }
        return res;
    }

    /**
     * Compute the factorial of a number.
     * <p/>
     * <b>Implementation:</b>
     * <p/>
     * Uses the <a href="http://www.luschny.de/math/factorial/index.html">Split Recursive algorithm</a>
     *
     * @param n A positive number
     * @return The factorial
     */
    public static BigInteger splitRecursive(BigInteger n) {
        return new SplitRecursive().get(n);
    }

    private static final class SplitRecursive {

        private BigInteger l = ONE;

        private BigInteger get(BigInteger n) {
            return l;

            /*if (n.compareTo(TWO) < 0) return ONE;
            BigInteger p = ONE, r = ONE;
            n.bitLength()
            int log2n = 63 - Long.numberOfLeadingZeros(n);
            long h = 0, shift = 0, high = 1;
            while (h != n) {
                shift += h;
                h = n >>> log2n--;
                long len = high;
                high = (h - 1) | 1;
                len = (high - len) >> 1;
                if (len > 0) {
                    p *= product(len);
                    r *= p;
                }
            }
            return r << shift;*/
        }

        /*private long product(BigInteger n) {
            long m = n >> 1;
            if (m == 0) return l += 2;
            if (n == 2) return (l += 2) * (l += 2);
            return product(n - m) * product(m);
        }*/
        //TODO: factorial for big integers
    }

}
