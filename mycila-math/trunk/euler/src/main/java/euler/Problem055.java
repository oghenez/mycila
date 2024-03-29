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

import com.mycila.math.number.BigInt;

import static java.lang.System.*;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.Assert.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=55
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem055 {

    public static void main(String[] args) throws Exception {
        long time = currentTimeMillis();
        final int maxNumber = 10000;
        final int maxIterations = 50;
        final Set<BigInt> lychrel = new TreeSet<BigInt>();
        final Set<BigInt> nonLychrel = new TreeSet<BigInt>();
        final Set<BigInt> stack = new TreeSet<BigInt>();
        for (int n = 0; n < maxNumber; n++) {
            BigInt test = BigInt.big(n);
            // do not do anything if this number as already be marked
            if (lychrel.contains(test))
                continue;
            // if it is not marked, we must find what it is in a maximum of 'maxIterations' iterations
            stack.add(test);
            BigInt reverse = test.digitsReversed();
            // iterate while we not found a palindrom in some maximum iterations
            for (int it = 1; it < maxIterations; it++) {
                test = test.add(reverse);
                reverse = test.digitsReversed();
                stack.add(test);
                if (test.equals(reverse) || nonLychrel.contains(test)) {
                    nonLychrel.addAll(stack);
                    stack.clear();
                    break;
                } else if (lychrel.contains(test)) {
                    lychrel.addAll(stack);
                    stack.clear();
                    break;
                }
            }
            if (!stack.isEmpty()) {
                lychrel.addAll(stack);
                stack.clear();
            }
        }

        System.out.println(lychrel);
        out.println(lychrel.size() + " in " + (currentTimeMillis() - time) + "ms");

        int count = 0;
        BigInt max = BigInt.big(maxNumber);
        for (BigInt l : lychrel) if (l.compareTo(max) <= 0) count++;

        out.println(count + " under " + maxNumber);
        assertEquals(249, count);
        assertEquals(10000, maxNumber);
    }
}

/*

We can iterate over all 10000 numbers, from 0 to 9999, and we will mark those which gives a palindromic number.
So avoid recomputing, we will mark all numbers.
In example, if we find that a number A gives a palindromic number B, we will mark A plus all numbers which give A.
If after 49 iterations we do not have a palindromic number, all numbers of each iterations will be marked as being Lychrel numbers.
Then, we just have to count (and display) all marked Lychrel numbers !

*/
