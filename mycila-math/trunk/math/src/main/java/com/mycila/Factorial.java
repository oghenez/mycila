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

import java.math.BigInteger;

/**
 * @author Mathieu Carbou
 */
public final class Factorial {

    private Factorial() {
    }

    public static long trivial(long n) {
        long res = n > 1 ? n : 1;
        while (n-- > 1) res *= n;
        return res;
    }

    public static BigInteger trivial(BigInteger n) {
        BigInteger f = BigInteger.ONE;
        while (n.compareTo(BigInteger.ONE) > 0) {
            f = f.multiply(n);
            n = n.subtract(BigInteger.ONE);
        }
        return f;
    }

    // Computes fact(a) / fact(b) for a > b. Example 12!/6! = (12+6)! / 6! = 12*11*...*7
    public static long trivialDiv(long a, long b) {
        b++;
        long res = a > 1 ? a : 1;
        while (a-- > b) res *= a;
        return res;
    }

    public static BigInteger trivialDiv(BigInteger a, BigInteger b) {
        BigInteger res = BigInteger.ONE;
        while (a.compareTo(b) > 0) {
            res = res.multiply(a);
            a = a.subtract(BigInteger.ONE);
        }
        return res;
    }

    // Split Recursive (http://www.luschny.de/math/factorial/index.html)
    public static long splitRecursive(long n) {
        return new SplitRecursive().get(n);
    }

    private static final class SplitRecursive {

        private long l = 1;

        private long get(long n) {
            if (n < 2) return 1;
            long p = 1, r = 1;
            //log2n = floor(log2(n));
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
