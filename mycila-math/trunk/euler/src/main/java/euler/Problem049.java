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
 * http://projecteuler.net/index.php?section=problems&id=49
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem049 {
    public static void main(String[] args) throws Exception {
        long time = currentTimeMillis();
        // create a sieve up to maximum prime number having 4 digits
        final Digits digits = Digits.base(10);
        final Sieve sieve = Sieve.to(9999);
        for (int i = 0, max = sieve.size(); i < max - 2; i++) {
            final int prime = sieve.get(i);
            if (prime > 1000 && prime < 9999
                    && sieve.contains(prime + 3330)
                    && sieve.contains(prime + 6660)
                    && digits.arePermutations(prime, prime + 3330, prime + 6660))
                System.out.println(prime + " " + (prime + 3330) + " " + (prime + 6660));
        }
        out.println(" in " + (currentTimeMillis() - time) + "ms");
    }

}
