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

import com.mycila.math.triplet.IntTriplet;
import com.mycila.math.triplet.LongTriplet;

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
    public static List<IntTriplet> triplet(int sum) {
        List<IntTriplet> set = new ArrayList<IntTriplet>();
        sum >>= 1;
        for (int m = 2, max = (int) (Math.sqrt(sum) + 1); m < max; m++) {
            if (sum % m == 0) {
                int sm = sum / m;
                while ((sm & 1) == 0) sm >>= 1;
                for (int k = (m & 1) == 1 ? m + 2 : m + 1, m2 = m << 1; k < m2 && k <= sm; k += 2) {
                    if (sm % k == 0 && Divisors.gcd(k, m) == 1) {
                        int d = sum / (k * m);
                        int n = k - m;
                        set.add(IntTriplet.of(d * (m * m - n * n), (d * m * n) << 1, d * (m * m + n * n)));
                    }
                    k += 2;
                }
            }
        }
        return set;
    }

    /**
     * A Pythagorean triplet is a set of three natural numbers, a < b < c, for which a^(2) + b^(2) = c^(2)
     *
     * @param sum a + b + c = sum
     * @return the triplets
     */
    public static List<LongTriplet> triplet(long sum) {
        List<LongTriplet> set = new ArrayList<LongTriplet>();
        sum >>= 1;
        for (long m = 2, max = (long) (Math.sqrt(sum) + 1); m < max; m++) {
            if (sum % m == 0) {
                long sm = sum / m;
                while ((sm & 1) == 0) sm >>= 1;
                for (long k = (m & 1) == 1 ? m + 2 : m + 1, m2 = m << 1; k < m2 && k <= sm; k += 2) {
                    if (sm % k == 0 && Divisors.gcd(k, m) == 1) {
                        long d = sum / (k * m);
                        long n = k - m;
                        set.add(LongTriplet.of(d * (m * m - n * n), (d * m * n) << 1, d * (m * m + n * n)));
                    }
                    k += 2;
                }
            }
        }
        return set;
    }

}
