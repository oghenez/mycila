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

import com.mycila.math.Digits;
import com.mycila.math.list.IntSequence;
import com.mycila.math.prime.Sieve;

import static java.lang.System.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=35
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem035 {
    public static void main(String[] args) throws Exception {
        final long time = currentTimeMillis();
        // prime list before 1000000
        final Sieve sieve = Sieve.to(1000000);
        final IntSequence circulars = new IntSequence(sieve.size());
        final Digits digits = Digits.base(10);
        // for each prime, check if it is a circular prime
        main:
        for (int i = 0, max = sieve.size(); i < max; i++) {
            int prime = sieve.get(i);
            // for 1-digit primes, direclty ad them
            if (prime < 10) {
                circulars.add(prime);
                continue;
            }
            // if the prime is already in the circular list, ignore it
            else if (circulars.contains(prime)) continue;
            // for N-digits primes, check if the digits are only 1, 3, 7 or 9
            int length = 0;
            for (int num = prime; num > 0; num /= 10) {
                int digit = num % 10;
                length++;
                if (digit == 2 || digit == 4 || digit == 5 || digit == 6 || digit == 8) continue main;
            }
            final IntSequence probableCircular = new IntSequence(length);
            probableCircular.add(prime);
            for (; length > 1; length--) {
                prime = digits.rotate(prime, 1);
                // if the number is prime, add it to the probable circular list if not already there
                if (sieve.contains(prime)) {
                    if (!probableCircular.contains(prime)) {
                        probableCircular.add(prime);
                    }
                } else {
                    // otherwise exit and check other number
                    continue main;
                }
            }
            // if we are here, it means all permutations are primes. So the primes are circular.
            out.println(probableCircular);
            circulars.add(probableCircular.toNativeArray());

        }

        out.println(circulars);
        out.println(circulars.size() + " in " + (currentTimeMillis() - time) + "ms");
    }
}

/*

We use the Sieve method to list all primes up to 1000000.

If a prime has one of 0, 2, 4, 5, 6 or 8, it means that a permutation of the number will end up to be divisible by 2 or 5.
So for a prime number to be circular, its digits can only be 1, 3, 7 or 9.

For each prime, we first get its digits and check if there are only 1, 3, 7 or 9.
Then we rotate its digits and check if the resulting number is in the prime list

*/

