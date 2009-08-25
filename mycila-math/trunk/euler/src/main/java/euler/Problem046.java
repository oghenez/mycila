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

import com.mycila.math.list.IntSequence;
import com.mycila.math.prime.PrimaltyTest;

import static java.lang.System.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=46
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem046 {
    public static void main(String[] args) throws Exception {
        final long time = currentTimeMillis();
        final IntSequence squares = new IntSequence(1000);
        squares.add(new int[]{1, 4, 9, 16, 25, 36, 49, 64});
        final IntSequence primes = new IntSequence(1000);
        primes.add(new int[]{2, 3, 5, 7});
        for (int i = 9; ; i++) {
            squares.add(i * i);
            if ((i & 1) == 1) {
                if (PrimaltyTest.millerRabin(i)) primes.add(i);
                else {
                    int pos = -1;
                    for (int pIndex = 1, max = primes.size(), p; pIndex < max && (p = primes.getQuick(pIndex)) < i; pIndex++) {
                        pos = squares.binarySearch((i - p) >> 1);
                        if (pos >= 0) {
                            //out.println(i + "=" + p + "+2*" + squares.getQuick(pos));
                            break;
                        }
                    }
                    if (pos < 0) {
                        out.println("Not valid for: " + i + " in " + (currentTimeMillis() - time) + "ms");
                        return;
                    }
                }
            }
        }
    }
}