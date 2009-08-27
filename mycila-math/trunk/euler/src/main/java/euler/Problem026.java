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

import com.mycila.RecuringCycle;
import com.mycila.math.number.BigInteger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;

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

        BigInteger cycle = BigInteger.big(142857);
        int res = 7, maxCycleLength = 6;
        // for each prime numbers, starting at the maximum possible value (999 and 008 are not primes)
        for (int p = 997; p > 7; p -= 2) {
            if (BigInteger.big(p).isPrime()) {
                // if p is prime, we check the least number that satisfy 10^l mod p = 1 
                for (int l = 1; l < p; l++) {
                    BigInteger[] qr = BigInteger.ten().pow(l).divideAndRemainder(BigInteger.big(p));
                    // qr[0] is the quotient. It is also equals to the cycle of 1/p
                    // qr[1] is the remainder.
                    if (qr[1].equals(BigInteger.one())) {
                        // we found the length l of the cycle of 1/p
                        System.out.println("1/" + p + " has a recuring cycle length of " + l + ": " + leftPad(qr[0].toString(), l, '0'));
                        System.out.println("1/" + p + " = " + BigDecimal.ONE.divide(BigDecimal.valueOf(p), 2000, RoundingMode.HALF_UP));
                        // we save these values if the current cycle is greater
                        if (maxCycleLength < l) {
                            maxCycleLength = l;
                            res = p;
                            cycle = qr[0];
                        }
                        break;
                    }
                }
            }
        }

        System.out.println("=== RESULT ===");

        System.out.println("1/" + res + " has a recuring cycle length of " + maxCycleLength + ": " + leftPad(cycle.toString(), maxCycleLength, '0') + " in " + (System.currentTimeMillis() - time) + "ms");
        System.out.println(BigDecimal.ONE.divide(BigDecimal.valueOf(res), 2000, RoundingMode.HALF_UP));

        System.out.println(RecuringCycle.of(983));
    }

    private static String leftPad(String s, int length, char c) {
        char[] missing = new char[length - s.length()];
        Arrays.fill(missing, c);
        return new String(missing).concat(s);
    }
}

// 983
