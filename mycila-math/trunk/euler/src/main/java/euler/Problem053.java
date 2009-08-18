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

import com.mycila.combination.Combinations;

import static java.lang.System.*;
import java.math.BigInteger;

/**
 * http://projecteuler.net/index.php?section=problems&id=53
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem053 {
    public static void main(String[] args) throws Exception {
        long time = currentTimeMillis();
        int count = 0;
        final BigInteger LIMIT = BigInteger.valueOf(1000000);
        final BigInteger HUNDRED = BigInteger.valueOf(100);
        final BigInteger TWO = BigInteger.valueOf(2);
        for (BigInteger n = BigInteger.valueOf(23); n.compareTo(HUNDRED) <= 0; n = n.add(BigInteger.ONE)) {
            for (BigInteger p = TWO, max = n.shiftRight(1); p.compareTo(max) <= 0; p = p.add(BigInteger.ONE)) {
                final BigInteger c = Combinations.binomial(n, p);
                if (c.compareTo(LIMIT) > 0) {
                    BigInteger o = n.subtract(p);
                    count += o.compareTo(p) == 0 ? 1 : 2;
                    //if(o.compareTo(p) == 0) System.out.println(MessageFormat.format("1x C({0},{1})=C({0},{2})={3}", n, p, o, c));
                    //else System.out.println(MessageFormat.format("2x C({0},{1})=C({0},{2})={3}", n, p, o, c));
                }
            }
        }
        out.println(count + " in " + (currentTimeMillis() - time) + "ms");
    }

}

/*

We need to compute all C(n,p) with 23 <= n <= 100 and 1 <= p =< n
We know that C(n,1)=n and C(n,n)=1 So we can reduce to 1 < p < n.
We can reduce the search knowing that C(n, p) = C(n, n-p). Thus: 1 < p <= n/2.
If C(n,p) > 1000000, we double count for p and n-p, except if n=2p, where we count only one.

*/