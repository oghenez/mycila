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
import com.mycila.PrimaltyTest;
import com.mycila.Sieve;
import com.mycila.combination.Combinations;
import com.mycila.sequence.IntSequence;

import static java.lang.System.*;
import java.util.List;

/**
 * http://projecteuler.net/index.php?section=problems&id=60
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem060 {

    public static void main(String[] args) throws Exception {
        final long time = currentTimeMillis();

        final int SELECTION_SIZE = 5;
        final Digits digits = Digits.base(10);

        // all combinations to select a couple of prime amongst the set size
        final List<int[]> couples = Combinations.combinations(SELECTION_SIZE, 2).asList();

        // in the sieve, we skip primes at index 0 and 2:
        // there are 2 and 5, and they cannot be used in a concatenation
        final IntSequence primes = Sieve.to(100000).asSequence();
        primes.remove(2);
        primes.remove(0);

        for (int[] primesPosition : Combinations.combinations(primes.size(), SELECTION_SIZE)) {
            boolean found = true;
            for (int[] couplePositions : couples) {
                final int p1 = primes.getQuick(primesPosition[couplePositions[0]]);
                final int p2 = primes.getQuick(primesPosition[couplePositions[1]]);
                if (!PrimaltyTest.trialDivision(digits.concat(p1, p2))
                        || !PrimaltyTest.trialDivision(digits.concat(p2, p1))) {
                    found = false;
                    break;
                }
            }
            if (found) {
                int sum = 0;
                for (int pos : primesPosition)
                    sum += primes.getQuick(pos);

                StringBuilder sb = new StringBuilder().append(sum).append(":");
                for (int pos : primesPosition)
                    sb.append(" ").append(primes.getQuick(pos));
                System.out.println(sb.append(" in ").append(currentTimeMillis() - time).append("ms"));
            }
        }
    }

}
