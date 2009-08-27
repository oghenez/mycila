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
package euler;

import com.mycila.math.number.BigInteger;

/**
 * http://projecteuler.net/index.php?section=problems&id=15
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem015 {
    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        // This is the problem of the Catalan number.
        // http://en.wikipedia.org/wiki/Catalan_number
        // C(n) = (2n)! / (n+1)!n!
        // We want to know all applications for a square of length L,
        // L+1 is the total number of exceedance decreasing steps
        // so the result will be C(L)*(L+1)
        // We call P(x,y) the product of the numbers between x to y.
        // Thus P(1,n) = n!
        // The numbers of toutes R(n) = [(n+1)*(2*n)!]/[(n+1)!*n!]
        // R(n) = [(n+1)*P(1,2n)]/[P(1,n+1)*P(1,n)]
        // R(n) = [(n+1)*P(1,n)*P(n+1,2n)]/[P(1,n+1)*P(1,n)]
        // R(n) = [(n+1)*P(n+1,2n)]/[P(1,n+1)]
        // R(n) = P(n+1,2n)/P(1,n)
        BigInteger r = p(21, 40).divide(p(1, 20));
        System.out.println(r + " in " + (System.currentTimeMillis() - time) + "ms");
    }

    private static BigInteger p(long a, long b) {
        BigInteger res = BigInteger.big(b);
        while (b-- > a) res = res.multiply(BigInteger.big(b));
        return res;
    }
}

// 137846528820
