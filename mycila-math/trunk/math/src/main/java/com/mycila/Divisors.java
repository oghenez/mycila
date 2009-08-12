package com.mycila;

/**
 * @author Mathieu Carbou
 */
public final class Divisors {

    private Divisors() {
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
        /* Let shift := lg K, where K is the greatest power of 2 dividing both u and v. */
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
        /* Let shift := lg K, where K is the greatest power of 2 dividing both u and v. */
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
