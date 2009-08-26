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

import com.mycila.Decomposition;
import com.mycila.math.prime.Sieve;

import static java.lang.System.*;
import java.util.ArrayList;
import java.util.List;

/**
 * http://projecteuler.net/index.php?section=problems&id=47
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem047 {
    public static void main(String[] args) throws Exception {
        final long time = currentTimeMillis();

        // will stock our results, just for display
        final List<Decomposition> results = new ArrayList<Decomposition>(4);
        final int[] numbers = new int[4];

        // create a prime sieve to reuse it to find factors to avoid repeated calls of tial division
        Sieve sieve = Sieve.to(100000);
        int max = sieve.last();

        // check all numbers starting at 2*3*5*7 which is the first composite of 4 different primes
        for (int n = 2 * 3 * 5 * 7, consecutive = 0; ; n++) {
            // if n is above the sieve limit, we extend the sieve up to 2n primes
            if (n > max) {
                sieve = sieve.grow(100000);
                max = sieve.last();
            }
            // then we extract the different prime factors of n
            Decomposition decomposition = Decomposition.of(n);
            // if the count is 4, we increment our counter. Otherwise, we reset it.
            if (decomposition.factorCount() == 4) {
                results.add(consecutive, decomposition);
                numbers[consecutive++] = n;
            } else consecutive = 0;
            if (consecutive == 4) {
                for (int i = 0; i < numbers.length; i++)
                    System.out.println(numbers[i] + ": " + results.get(i));
                out.println(" in " + (currentTimeMillis() - time) + "ms");
                break;
            }
        }
    }
}

/*
134043: [3^1, 7^1, 13^1, 491^1]
134044: [2^2, 23^1, 31^1, 47^1]
134045: [5^1, 17^1, 19^1, 83^1]
134046: [2^1, 3^2, 11^1, 677^1]
 in 3890ms
*/