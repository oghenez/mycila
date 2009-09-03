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

import static com.mycila.math.Format.*;
import com.mycila.math.number.BigInt;
import static com.mycila.math.number.BigInt.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=26
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem026 {
    public static void main(String[] args) throws Exception {
        long time = System.currentTimeMillis();

        // http://en.wikipedia.org/wiki/Recurring_decimal#Fraction_1.E2.81.84pk

        // A fraction in lowest terms with a prime denominator other than 2 or 5 (i.e. coprime to 10) always produces a repeating decimal.
        // The repeating decimal 1/p, where p is prime, is either a cyclic number or other than a cyclic number,
        // depending respectively whether its period is: p − 1 or a factor of p − 1
        // also, the cycle's lenth L is such that 10^L mod p = 1 for prime number in 1/p, L being as small as possible and lower than p. 

        BigInt cycle = big(142857);
        BigInt res = big(7), maxCycleLength = big(6);
        // for each prime numbers, starting at the maximum possible value (999 and 008 are not primes)
        for (int p = 997; p > 7; p -= 2) {
            BigInt bp = big(p);
            if (bp.isPrime()) {
                BigInt[] cl = bp.recuringCycle();
                if (maxCycleLength.compareTo(cl[1]) < 0) {
                    System.out.println("1/" + bp + " has a recuring cycle length of " + cl[1] + ": " + leftPad(cl[0].toString(), cl[1].toInt(), '0'));
                    maxCycleLength = cl[1];
                    res = bp;
                    cycle = cl[0];
                }
            }
        }

        System.out.println("=== RESULT ===");

        System.out.println("1/" + res + " has a recuring cycle length of " + maxCycleLength + ": " + leftPad(cycle.toString(), maxCycleLength.toInt(), '0') + " in " + (System.currentTimeMillis() - time) + "ms");

    }

}

// 983
