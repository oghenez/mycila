package com.mycila;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mathieu Carbou
 */
public final class Pythagore {

    private Pythagore() {
    }

    /**
     * A Pythagorean triplet is a set of three natural numbers, a < b < c, for which a^(2) + b^(2) = c^(2)
     *
     * @param sum a + b + c = sum
     * @return the triplets
     */
    public static List<Triplet> triplet(int sum) {
        List<Triplet> set = new ArrayList<Triplet>();
        sum >>= 1;
        for (int m = 2, max = (int) (Math.sqrt(sum) + 1); m < max; m++) {
            if (sum % m == 0) {
                int sm = sum / m;
                while ((sm & 1) == 0) sm >>= 1;
                for (int k = (m & 1) == 1 ? m + 2 : m + 1, m2 = m << 1; k < m2 && k <= sm; k += 2) {
                    if (sm % k == 0 && Divisors.gcd(k, m) == 1) {
                        int d = sum / (k * m);
                        int n = k - m;
                        set.add(Triplet.of(d * (m * m - n * n), (d * m * n) << 1, d * (m * m + n * n)));
                    }
                    k += 2;
                }
            }
        }
        return set;
    }

}
