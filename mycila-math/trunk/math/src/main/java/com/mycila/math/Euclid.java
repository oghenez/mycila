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

/**
 * @author Mathieu Carbou
 */
public final class Euclid {

    private Euclid() {
    }

    /**
     * Compute the <a href="http://en.wikipedia.org/wiki/Extended_Euclidean_algorithm">Extended Euclidean algorithm</a>.
     * <p/>
     * <b>Implementation:</b>
     * <p/>
     * Uses <a href="http://en.literateprograms.org/Extended_Euclidean_algorithm_(Python)">this algorithm</a>
     * <p/>
     * <b>Notes:</b>
     * <p/>
     * For u and v, this algorithm finds (a, b, c) such that <code>ua + vb = c = gcd(u,v)</code>.
     *
     * @param u a number
     * @param v a number
     * @return The triplet (a, b, c)
     */
    public static IntTriplet extended(int u, int v) {
        int u1 = 1,
                u2 = 0,
                u3 = u,
                v1 = 0,
                v2 = 1,
                v3 = v;
        while (v3 != 0) {
            int q = u3 / v3;
            int t1 = u1 - q * v1;
            int t2 = u2 - q * v2;
            int t3 = u3 - q * v3;
            u1 = v1;
            u2 = v2;
            u3 = v3;
            v1 = t1;
            v2 = t2;
            v3 = t3;
        }
        return IntTriplet.of(u1, u2, u3);
    }

    /**
     * Compute the <a href="http://en.wikipedia.org/wiki/Extended_Euclidean_algorithm">Extended Euclidean algorithm</a>.
     * <p/>
     * <b>Implementation:</b>
     * <p/>
     * Uses <a href="http://en.literateprograms.org/Extended_Euclidean_algorithm_(Python)">this algorithm</a>
     * <p/>
     * <b>Notes:</b>
     * <p/>
     * For u and v, this algorithm finds (a, b, c) such that <code>ua + vb = c = gcd(u,v)</code>.
     *
     * @param u a number
     * @param v a number
     * @return The triplet (a, b, c)
     */
    public static LongTriplet extended(long u, long v) {
        long u1 = 1,
                u2 = 0,
                u3 = u,
                v1 = 0,
                v2 = 1,
                v3 = v;
        while (v3 != 0) {
            long q = u3 / v3;
            long t1 = u1 - q * v1;
            long t2 = u2 - q * v2;
            long t3 = u3 - q * v3;
            u1 = v1;
            u2 = v2;
            u3 = v3;
            v1 = t1;
            v2 = t2;
            v3 = t3;
        }
        return LongTriplet.of(u1, u2, u3);
    }

}
