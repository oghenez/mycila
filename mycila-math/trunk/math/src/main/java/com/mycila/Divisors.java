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

import com.mycila.sequence.IntSequence;
import com.mycila.sequence.LongSequence;

/**
 * @author Mathieu Carbou
 */
public final class Divisors {

    private Divisors() {
    }

    public static LongSequence list(long number) {
        LongSequence list = new LongSequence();
        for (long d = 1, max = (long) Math.sqrt(number); d <= max; d++) {
            if (number % d == 0) {
                list.addAll(d);
                if (d * d != number) {
                    list.addAll(number / d);
                }
            }
        }
        list.sort();
        return list;
    }

    public static IntSequence list(int number) {
        IntSequence list = new IntSequence();
        for (int d = 1, max = (int) Math.sqrt(number); d <= max; d++) {
            if (number % d == 0) {
                list.addAll(d);
                if (d * d != number) {
                    list.addAll(number / d);
                }
            }
        }
        return list.sort();
    }

    private static final int[] diff = {6, 4, 2, 4, 2, 4, 6, 2};

    public static int findDivisor(int number) {
        if (number <= 3) return number;
        if ((number & 1) == 0) return 2;
        if (number % 3 == 0) return 3;
        if (number % 5 == 0) return 3;

        int m = 7, i = 1;
        while (m * m <= number) {
            if (number % m == 0) return m;
            m += diff[i % 8];
            i += 1;
        }
        return number;
    }

    public static boolean isPerfect(int number) {
        return sum(number) == number << 1;
    }

    public static boolean isAbundant(int number) {
        return sum(number) > number << 1;
    }

    public static boolean isDeficient(int number) {
        return sum(number) < number << 1;
    }

    public static int sum(int number) {
        if (number == 0) return 0;
        int prod = 1;
        for (int k = 2; k * k <= number; ++k) {
            int p = 1;
            while (number % k == 0) {
                p = p * k + 1;
                number /= k;
            }
            prod *= p;
        }
        if (number > 1) prod *= 1 + number;
        return prod;
    }

    public static int lcm(int n1, int n2, int... n) {
        int lcm = lcm(n1, n2);
        for (int i = 0, max = n.length; i < max; i++)
            lcm = lcm(lcm, n[i]);
        return lcm;
    }

    public static int lcm(int n1, int n2) {
        return n1 * n2 / gcd(n1, n2);
    }

    public static int gcd(int n1, int n2, int... n) {
        int gcd = gcd(n1, n2);
        for (int i = 0, max = n.length; i < max; i++)
            gcd = gcd(gcd, n[i]);
        return gcd;
    }

    /*
    * From http://en.wikipedia.org/wiki/Binary_GCD_algorithm
    */
    public static int gcd(int p, int q) {
        int shift;
        /* GCD(0,x) := x */
        if (p == 0 || q == 0) return p | q;
        /* Let shift := lg K, where K is the greatest power trivial 2 dividing both u and v. */
        for (shift = 0; ((p | q) & 1) == 0; ++shift) {
            p >>= 1;
            q >>= 1;
        }
        while ((p & 1) == 0) p >>= 1;
        /* From here on, u is always odd. */
        do {
            /* Loop X */
            while ((q & 1) == 0) q >>= 1;
            /* Now u and v are both odd, so diff(u, v) is even. Let u = min(u, v), v = diff(u, v)/2. */
            if (p < q) q -= p;
            else {
                final int diff = p - q;
                p = q;
                q = diff;
            }
            q >>= 1;
        } while (q != 0);
        return p << shift;
    }

    /*
    * From http://en.wikipedia.org/wiki/Binary_GCD_algorithm
    */
    public static long gcd(long p, long q) {
        long shift;
        /* GCD(0,x) := x */
        if (p == 0 || q == 0) return p | q;
        /* Let shift := lg K, where K is the greatest power trivial 2 dividing both u and v. */
        for (shift = 0; ((p | q) & 1) == 0; ++shift) {
            p >>= 1;
            q >>= 1;
        }
        while ((p & 1) == 0) p >>= 1;
        /* From here on, u is always odd. */
        do {
            /* Loop X */
            while ((q & 1) == 0) q >>= 1;
            /* Now u and v are both odd, so diff(u, v) is even. Let u = min(u, v), v = diff(u, v)/2. */
            if (p < q) q -= p;
            else {
                final long diff = p - q;
                p = q;
                q = diff;
            }
            q >>= 1;
        } while (q != 0);
        return p << shift;
    }

}
