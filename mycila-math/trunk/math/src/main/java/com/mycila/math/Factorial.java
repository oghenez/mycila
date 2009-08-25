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

/**
 * @author Mathieu Carbou
 */
public final class Factorial {

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
    public static long trivial(long number) {
        long res = number > 1 ? number : 1;
        while (number-- > 1) res *= number;
        return res;
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
        BigInteger f = BigInteger.ONE;
        while (n.signum() > 0) {
            f = f.multiply(n);
            n = n.subtract(BigInteger.ONE);
        }
        return f;
    }

    /**
     * Computes <code>fact(a) / fact(b) for a > b</code>
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
     * Computes <code>fact(a) / fact(b) for a > b</code>
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
        BigInteger res = BigInteger.ONE;
        while (a.compareTo(b) > 0) {
            res = res.multiply(a);
            a = a.subtract(BigInteger.ONE);
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
    public static long splitRecursive(long n) {
        return new SplitRecursive().get(n);
    }

    private static final class SplitRecursive {

        private long l = 1;

        private long get(long n) {
            if (n < 2) return 1;
            long p = 1, r = 1;
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
            return r << shift;
        }

        private long product(long n) {
            long m = n >> 1;
            if (m == 0) return l += 2;
            if (n == 2) return (l += 2) * (l += 2);
            return product(n - m) * product(m);
        }
    }

}
