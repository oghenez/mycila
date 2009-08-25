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

import com.mycila.Sieve;
import com.mycila.math.sequence.IntSequence;

import static java.lang.System.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=60
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem060 {

    public static void main(String[] args) throws Exception {
        final long time = currentTimeMillis();

        final Sieve sieve = Sieve.to(9999);

        // remove primes which cannot be concatenated
        IntSequence sequence = sieve.asSequence();
        sequence.remove(2);
        sequence.remove(0);
        final int[] primes = sequence.toNativeArray();

        main:
        for (int p1 = 0, p1max = primes.length - 5; p1 < p1max; p1++) {
            for (int p2 = p1 + 1, p2max = p1max + 1; p2 < p2max; p2++) {
                if (!sieve.isPrime(Integer.parseInt(primes[p1] + "" + primes[p2]))
                        || !sieve.isPrime(Integer.parseInt(primes[p2] + "" + primes[p1])))
                    continue;
                for (int p3 = p2 + 1, p3max = p2max + 1; p3 < p3max; p3++) {
                    if (!sieve.isPrime(Integer.parseInt(primes[p1] + "" + primes[p3]))
                            || !sieve.isPrime(Integer.parseInt(primes[p3] + "" + primes[p1]))
                            || !sieve.isPrime(Integer.parseInt(primes[p2] + "" + primes[p3]))
                            || !sieve.isPrime(Integer.parseInt(primes[p3] + "" + primes[p2])))
                        continue;
                    for (int p4 = p3 + 1, p4max = p3max + 1; p4 < p4max; p4++) {
                        if (!sieve.isPrime(Integer.parseInt(primes[p1] + "" + primes[p4]))
                                || !sieve.isPrime(Integer.parseInt(primes[p2] + "" + primes[p4]))
                                || !sieve.isPrime(Integer.parseInt(primes[p3] + "" + primes[p4]))
                                || !sieve.isPrime(Integer.parseInt(primes[p4] + "" + primes[p1]))
                                || !sieve.isPrime(Integer.parseInt(primes[p4] + "" + primes[p2]))
                                || !sieve.isPrime(Integer.parseInt(primes[p4] + "" + primes[p3])))
                            continue;
                        for (int p5 = p4 + 1, p5max = p4max + 1; p5 < p5max; p5++) {
                            if (!sieve.isPrime(Integer.parseInt(primes[p1] + "" + primes[p5]))
                                    || !sieve.isPrime(Integer.parseInt(primes[p2] + "" + primes[p5]))
                                    || !sieve.isPrime(Integer.parseInt(primes[p3] + "" + primes[p5]))
                                    || !sieve.isPrime(Integer.parseInt(primes[p4] + "" + primes[p5]))
                                    || !sieve.isPrime(Integer.parseInt(primes[p5] + "" + primes[p1]))
                                    || !sieve.isPrime(Integer.parseInt(primes[p5] + "" + primes[p2]))
                                    || !sieve.isPrime(Integer.parseInt(primes[p5] + "" + primes[p3]))
                                    || !sieve.isPrime(Integer.parseInt(primes[p5] + "" + primes[p4])))
                                continue;
                            System.out.println(primes[p1] + " " + primes[p2] + " " + primes[p3] + " " + primes[p4] + " " + primes[p5] + " => " +
                                    (primes[p1] + primes[p2] + primes[p3] + primes[p4] + primes[p5]));
                            break main;
                        }
                    }
                }
            }
        }
        System.out.println("in " + (System.currentTimeMillis() - time) + "ms");
    }

}
