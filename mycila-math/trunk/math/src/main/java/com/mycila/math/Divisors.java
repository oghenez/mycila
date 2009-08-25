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

import com.mycila.math.list.IntSequence;
import com.mycila.math.list.LongSequence;

import java.math.BigInteger;

/**
 * @author Mathieu Carbou
 */
public final class Divisors {

    private static final int[] diff = {6, 4, 2, 4, 2, 4, 6, 2};

    private Divisors() {
    }

    /**
     * List all divisors of a number in ascending order
     *
     * @param number The number
     * @return The divisor list
     */
    public static IntSequence list(int number) {
        IntSequence list = new IntSequence();
        for (int d = 1, max = (int) Math.sqrt(number); d <= max; d++) {
            if (number % d == 0) {
                list.add(d);
                if (d * d != number)
                    list.add(number / d);
            }
        }
        return list.sort();
    }

    /**
     * List all divisors of a number in ascending order
     *
     * @param number The number
     * @return The divisor list
     */
    public static LongSequence list(long number) {
        LongSequence list = new LongSequence();
        for (long d = 1, max = (long) Math.sqrt(number); d <= max; d++) {
            if (number % d == 0) {
                list.add(d);
                if (d * d != number)
                    list.add(number / d);
            }
        }
        list.sort();
        return list;
    }

    /**
     * Try to find a divisor of given number.
     *
     * @param number The number
     * @return The first divisor found, or number if number is prime
     */
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

    /**
     * Try to find a divisor of given number.
     *
     * @param number The number
     * @return The first divisor found, or number if number is prime
     */
    public static long findDivisor(long number) {
        if (number <= 3) return number;
        if ((number & 1) == 0) return 2;
        if (number % 3 == 0) return 3;
        if (number % 5 == 0) return 3;
        long m = 7, i = 1;
        while (m * m <= number) {
            if (number % m == 0) return m;
            m += diff[(int) (i % 8)];
            i += 1;
        }
        return number;
    }

    /**
     * Get the sum of all divisors of a number
     *
     * @param number The number
     * @return The number's divisor sum
     */
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

    /**
     * Get the sum of all divisors of a number
     *
     * @param number The number
     * @return The number's divisor sum
     */
    public static long sum(long number) {
        if (number == 0) return 0;
        long prod = 1;
        for (long k = 2; k * k <= number; ++k) {
            long p = 1;
            while (number % k == 0) {
                p = p * k + 1;
                number /= k;
            }
            prod *= p;
        }
        if (number > 1) prod *= 1 + number;
        return prod;
    }

    /**
     * Returns the least common multiple between all provided numbers
     *
     * @param n1 A number
     * @param n2 A number
     * @param n  Other  numbers
     * @return lcm(n1,n2,...n)
     */
    public static int lcmInt(int n1, int n2, int... n) {
        int lcm = lcm(n1, n2);
        for (int i = 0, max = n.length; i < max; i++)
            lcm = lcm(lcm, n[i]);
        return lcm;
    }

    /**
     * Returns the least common multiple between all provided numbers
     *
     * @param n1 A number
     * @param n2 A number
     * @param n  Other numbers
     * @return lcm(n1,n2,...n)
     */
    public static long lcmLong(long n1, long n2, long... n) {
        long lcm = lcm(n1, n2);
        for (int i = 0, max = n.length; i < max; i++)
            lcm = lcm(lcm, n[i]);
        return lcm;
    }

    /**
     * Returns the least common multiple between all provided numbers
     *
     * @param n1 A number
     * @param n2 A number
     * @param n  Other numbers
     * @return lcm(n1,n2,...n)
     */
    public static BigInteger lcm(BigInteger n1, BigInteger n2, BigInteger... n) {
        BigInteger lcm = lcm(n1, n2);
        for (int i = 0, max = n.length; i < max; i++)
            lcm = lcm(lcm, n[i]);
        return lcm;
    }

    /**
     * Returns the least common multiple between two numbers
     *
     * @param n1 A number
     * @param n2 A number
     * @return lcm(n1,n2)
     */
    public static int lcm(int n1, int n2) {
        return (n1 / gcd(n1, n2)) * n2;
    }

    /**
     * Returns the least common multiple between two numbers
     *
     * @param n1 A number
     * @param n2 A number
     * @return lcm(n1,n2)
     */
    public static long lcm(long n1, long n2) {
        return (n1 / gcd(n1, n2)) * n2;
    }

    /**
     * Returns the least common multiple between two numbers
     *
     * @param n1 A number
     * @param n2 A number
     * @return lcm(n1,n2)
     */
    public static BigInteger lcm(BigInteger n1, BigInteger n2) {
        return n1.divide(n1.gcd(n2)).multiply(n2);
    }

    /**
     * Returns the greatest common divisor between two numbers or plus
     *
     * @param n1 A number
     * @param n2 A number
     * @param n  Other numbers
     * @return gcd(n1,n2,...n)
     */
    public static int gcdInt(int n1, int n2, int... n) {
        int gcd = gcd(n1, n2);
        for (int i = 0, max = n.length; i < max; i++)
            gcd = gcd(gcd, n[i]);
        return gcd;
    }

    /**
     * Returns the greatest common divisor between two numbers or plus
     *
     * @param n1 A number
     * @param n2 A number
     * @param n  Other numbers
     * @return gcd(n1,n2,...n)
     */
    public static long gcdLong(long n1, long n2, long... n) {
        long gcd = gcd(n1, n2);
        for (int i = 0, max = n.length; i < max; i++)
            gcd = gcd(gcd, n[i]);
        return gcd;
    }

    /**
     * Returns the greatest common divisor between two numbers or plus
     *
     * @param n1 A number
     * @param n2 A number
     * @param n  Other numbers
     * @return gcd(n1,n2,...n)
     */
    public static BigInteger gcd(BigInteger n1, BigInteger n2, BigInteger... n) {
        BigInteger gcd = gcd(n1, n2);
        for (int i = 0, max = n.length; i < max; i++)
            gcd = gcd(gcd, n[i]);
        return gcd;
    }

    /**
     * Returns the greatest common divisor between two numbers.<br>
     * Implementation from <a href="http://en.wikipedia.org/wiki/Binary_GCD_algorithm">http://en.wikipedia.org/wiki/Binary_GCD_algorithm</a>
     *
     * @param p A number
     * @param q A number
     * @return gcd(n1,n2)
     */
    public static BigInteger gcd(BigInteger p, BigInteger q) {
        return p.gcd(q);
    }

    /**
     * Returns the greatest common divisor between two numbers.
     *
     * @param p A number
     * @param q A number
     * @return gcd(n1,n2)
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

    /**
     * Returns the greatest common divisor between two numbers.<br>
     * Implementation from <a href="http://en.wikipedia.org/wiki/Binary_GCD_algorithm">http://en.wikipedia.org/wiki/Binary_GCD_algorithm</a>
     *
     * @param p A number
     * @param q A number
     * @return gcd(n1,n2)
     */
    public static long gcd(long p, long q) {
        long shift;
        /* GCD(0,x) := x */
        if (p == 0 || q == 0) return p | q;
        /* Let shift := lg K, where K is the greatest power get 2 dividing both u and v. */
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

    /**
     * Check wheter this number is a <a href="http://en.wikipedia.org/wiki/Perfect_number">perfect number</a>
     *
     * @param number The number
     * @return true if it is perfect
     */
    public static boolean isPerfect(long number) {
        return sum(number) == number << 1;
    }

    /**
     * Check wheter this number is an <a href="http://en.wikipedia.org/wiki/Abundant_number">abundant number</a>
     *
     * @param number The number
     * @return true if it is abundant
     */
    public static boolean isAbundant(long number) {
        return sum(number) > number << 1;
    }

    /**
     * Check wheter this number is a <a href="http://en.wikipedia.org/wiki/Deficient_number">deficient number</a>
     *
     * @param number The number
     * @return true if it is deficient
     */
    public static boolean isDeficient(long number) {
        return sum(number) < number << 1;
    }

}
