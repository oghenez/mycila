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

import com.mycila.Digits;
import com.mycila.Sieve;

import static java.lang.System.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=60
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem060 {

    public static void main(String[] args) throws Exception {
        final long time = currentTimeMillis();

        final int SET_SIZE = 4;
        final Digits digits = Digits.base(10);
        final Sieve sieve = Sieve.to(10000);
        /*for (List<Integer> primes : Combinations.combinations(SET_SIZE, sieve.asList())) {
            // skip set containing 2 or 5 since concatenations will be multiples
            if (primes.contains(2) || primes.contains(5)) continue;
            boolean found = true;
            for (List<Integer> couple : Combinadic.base(SET_SIZE).combinations(2, primes)) {
                int p1 = couple.get(0);
                int p2 = couple.get(1);
                if (!PrimaltyTest.trialDivision(p1 * (int) Math.pow(10, digits.length(p2)) + p2)
                        || !PrimaltyTest.trialDivision(p2 * (int) Math.pow(10, digits.length(p1)) + p1)) {
                    found = false;
                    break;
                }
            }
            if (found) {
                int sum = 0;
                for (int prime : primes) sum += prime;
                System.out.println(primes + " => " + sum);
            }
        }*/

        out.println("in " + (currentTimeMillis() - time) + "ms");
    }

}
