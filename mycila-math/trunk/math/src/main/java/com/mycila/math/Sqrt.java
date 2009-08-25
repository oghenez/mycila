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
public final class Sqrt {

    private static final BigInteger TWO = valueOf(2);
    private static final BigInteger THREE = valueOf(3);

    private Sqrt() {
    }

    //TODO: add intsqrt from http://atoms.alife.co.uk/sqrt/index.html + execute tests + perf

    /**
     * Compute the <a href="http://en.wikipedia.org/wiki/Integer_square_root">integer square root</a> of a number.
     * <p/>
     * <b>Implementation:</b>
     * <p/>
     * Uses the <a href="http://en.wikipedia.org/wiki/Methods_of_computing_square_roots#Binary_.28base_2.29">base 2 algorithm</a>
     * <p/>
     * <b>Notes:</b>
     * <p/>
     * Slower than <code>int sqrt = (int) (Math.sqrt(i));</code>
     *
     * @param number A positive number from 0 to {@link Integer#MAX_VALUE}
     * @return The integer square root in an int
     */
    public static int sqrtInt(int number) {
        if (number < 2) return number;
        int res = 0;
        int one = 1 << 30;
        while (one > number) one >>= 2;
        while (one != 0) {
            final int sum = res + one;
            if (number < sum) res >>= 1;
            else {
                number -= sum;
                res = (res >> 1) + one;
            }
            one >>= 2;
        }
        return res;
    }

    /**
     * Compute the <a href="http://en.wikipedia.org/wiki/Integer_square_root">integer square root</a> of a number.
     * <p/>
     * <b>Implementation:</b>
     * <p/>
     * Uses the <a href="http://en.wikipedia.org/wiki/Methods_of_computing_square_roots#Binary_.28base_2.29">base 2 algorithm</a>
     * <p/>
     * <b>Notes:</b>
     * <p/>
     * Slower than <code>long sqrt = (long) (Math.sqrt(i));</code>
     *
     * @param number A positive number from 0 to {@link Long#MAX_VALUE}
     * @return The integer square root in an int
     */
    public static long sqrtInt(long number) {
        long res = 0;
        long one = 1L << 62;
        while (one > number) one >>= 2;
        while (one != 0) {
            final long sum = res + one;
            if (number < sum) res >>= 1;
            else {
                number -= sum;
                res = (res >> 1) + one;
            }
            one >>= 2;
        }
        return res;
    }

    /**
     * Compute the <a href="http://en.wikipedia.org/wiki/Integer_square_root">integer square root</a> of a number.
     *
     * @param number A positive number
     * @return The integer square root
     */
    public static BigInteger sqrtInt(BigInteger number) {
        BigInteger square = BigInteger.ONE;
        BigInteger delta = THREE;
        while (square.compareTo(number) <= 0) {
            square = square.add(delta);
            delta = delta.add(TWO);
        }
        return delta.shiftRight(1).subtract(BigInteger.ONE);
    }
}
