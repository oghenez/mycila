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

import com.mycila.math.number.BigInt;

import static java.lang.Math.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Fibonacci {

    private static final double SQRT_5 = sqrt(5);
    private static final double GOLD = (1.0 + SQRT_5) / 2.0;

    private static final int MAX_FIB_FOR_LONG = 92;

    //TODO: cf bigal-2.4.0.zip

    public static long fibonacciNext(long fibonacci) {
        return round(fibonacci * GOLD);
    }

    public static boolean isFibonaci(long n) {
        double sqrt1 = sqrt(5 * n * n + 4);
        if (sqrt1 != (long) sqrt1) {
            double sqrt2 = sqrt(5 * n * n - 4);
            return sqrt2 == (long) sqrt2;
        }
        return true;
    }

    public static boolean isFibonaci(int n) {
        double sqrt1 = sqrt(5 * n * n + 4);
        if (sqrt1 != (int) sqrt1) {
            double sqrt2 = sqrt(5 * n * n - 4);
            return sqrt2 == (int) sqrt2;
        }
        return true;
    }

    public static long binet(int i) {
        return round(pow(GOLD, i) / SQRT_5);
    }

    public static BigInt iterative(long i) {
        if (i <= MAX_FIB_FOR_LONG) {
            long first = 0, second = 1;
            while (i-- > 0) {
                long tmp = first + second;
                first = second;
                second = tmp;
            }
            return BigInt.big(first);
        } else {
            BigInt first = BigInt.ZERO, second = BigInt.ONE;
            while (i-- > 0) {
                BigInt tmp = first.add(second);
                first = second;
                second = tmp;
            }
            return first;
        }
    }

    public static BigInt logarithmic(long i) {
        if (i > MAX_FIB_FOR_LONG)
            throw new UnsupportedOperationException("Fibonacci.Logarithmic() not implemented for numbers > " + MAX_FIB_FOR_LONG);
        long a = 1, b = 0;
        long c = 0x8000;
        while (c > 0) {
            long aa = (i & c) != 0 ? ((b << 1) + a) * a : a * a + b * b;
            b = (i & c) != 0 ? a * a + b * b : b * ((a << 1) - b);
            a = aa;
            c >>>= 1;
        }
        return BigInt.big(b);
    }

}
