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
 * http://projecteuler.net/index.php?section=problems&id=37
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem037 {
    public static void main(String[] args) throws Exception {
        final long time = currentTimeMillis();
        final IntSequence truncables = new IntSequence(11);
        Sieve sieve = Sieve.to(1000000);
        // sieve(8)=23 is the first truncable prime since 1x is not truncable (1 not prime)
        for (int i = 8; truncables.size() < 11; i++) {
            if (i == sieve.size()) sieve = sieve.grow(10000);
            int mask = 0;
            // check by truncating right first, which will give us
            // the number's length to truncate left after
            int prime = sieve.get(i);
            boolean checkComplete = false;
            boolean truncable = true;
            for (; !checkComplete && (prime /= 10) > 0; mask++)
                if (!sieve.contains(prime))
                    checkComplete = !(truncable = false);
            // - If checkComplete is true, it means the prime has already been
            // added to our truncate list, or it is not a truncable prime.
            // - If checkComplete is false, it means all truncations from right
            // passed the test, and we know truncate from left
            if (!checkComplete) {
                prime = sieve.get(i);
                mask = (int) Math.pow(10, mask);
                for (; !checkComplete && mask >= 10; mask /= 10)
                    if (!sieve.contains(prime %= mask))
                        checkComplete = !(truncable = false);
            }
            // now we know are numner is truncable
            if (truncable) truncables.add(sieve.get(i));
        }
        out.println(truncables.sum() + " : " + truncables + " in " + (currentTimeMillis() - time) + "ms");
    }
}
