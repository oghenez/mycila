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
import com.mycila.math.list.LongSequence;

import static java.lang.System.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=43
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem043 {
    public static void main(String[] args) throws Exception {
        final long time = currentTimeMillis();

        // search for combinations of multiples of 13 (d7d8d9) and 17 (d8d9d10) to build d7d8d9d10
        final Digits digits = Digits.base(10);
        final IntSequence list_d7d8d9d10 = new IntSequence();
        for (int d8d9d10 = 17; d8d9d10 < 987; d8d9d10 += 17) { // 017 to 987
            for (int d7d8d9 = 13; d7d8d9 < 987; d7d8d9 += 13) { // 013 to 987
                if (d7d8d9 % 100 == d8d9d10 / 10) {
                    final int d7d8d9d10 = d7d8d9 * 10 + d8d9d10 % 10;
                    if (digits.allDifferents(d7d8d9d10, 4)) {
                        final int offset = list_d7d8d9d10.binarySearch(d7d8d9d10);
                        if (offset < 0) list_d7d8d9d10.insert(-offset - 1, d7d8d9d10);
                    }
                }
            }
        }
        System.out.println("d7d8d9d10: " + list_d7d8d9d10);

        // search for multiples of 2 (d2d3d4), 3 (d3d4d5) and 5 (d4d5d6) to build d2d3d4d5d6
        final IntSequence list_d2d3d4d5d6 = new IntSequence();
        for (int d4d5d6 = 15; d4d5d6 < 987; d4d5d6 += 10) { // 015 to 987, skipping cases where d6 = 0
            for (int d2d3d4 = 12; d2d3d4 < 987; d2d3d4 += 2) { // 012 to 987
                final int d4 = d2d3d4 % 10;
                // if d4 is the same digit in both numbers, we can combine them and check
                // if d3d4d5 is a multiple of 3
                if (d4d5d6 / 100 == d4 && ((d2d3d4 % 100) / 10 + d4 + (d4d5d6 % 100) / 10) % 3 == 0) {
                    final int d2d3d4d5d6 = d2d3d4 * 100 + d4d5d6 % 100;
                    if (digits.allDifferents(d2d3d4d5d6, 5)) {
                        int offset = list_d2d3d4d5d6.binarySearch(d2d3d4d5d6);
                        if (offset < 0) list_d2d3d4d5d6.insert(-offset - 1, d2d3d4d5d6);
                    }
                }
            }
        }
        System.out.println("d2d3d4d5d6: " + list_d2d3d4d5d6);

        // now builds the whole number
        final LongSequence list_d1d2d3d4d5d6d7d8d9d10 = new LongSequence();
        final Digits pandigital = Digits.base(10);
        for (int i = 0, max_i = list_d2d3d4d5d6.size(); i < max_i; i++) {
            for (int j = 0, max_j = list_d7d8d9d10.size(); j < max_j; j++) {
                final int d2d3d4d5d6 = list_d2d3d4d5d6.getQuick(i);
                final int d7d8d9d10 = list_d7d8d9d10.getQuick(j);
                if (((d7d8d9d10 / 100) % 10 - d7d8d9d10 / 1000 + (d2d3d4d5d6 % 10)) % 11 == 0
                        && ((d2d3d4d5d6 % 100) * 10 + d7d8d9d10 / 1000) % 7 == 0) {
                    for (long d1 = 1; d1 <= 9; d1++) { // for each d1 from 0 to 9
                        final long d1d2d3d4d5d6d7d8d9d10 = (d1 * 100000L + d2d3d4d5d6) * 10000L + d7d8d9d10;
                        if (pandigital.isPandigital(d1d2d3d4d5d6d7d8d9d10, 0, 9)) {
                            System.out.println(d1d2d3d4d5d6d7d8d9d10);
                            final int offset = list_d1d2d3d4d5d6d7d8d9d10.binarySearch(d1d2d3d4d5d6d7d8d9d10);
                            if (offset < 0) list_d1d2d3d4d5d6d7d8d9d10.add(d1d2d3d4d5d6d7d8d9d10);
                        }
                    }
                }
            }
        }
        System.out.println("d1d2d3d4d5d6d7d8d9d10: " + list_d1d2d3d4d5d6d7d8d9d10);

        // compute the sum
        long sum = 0;
        for (int i = 0, max_i = list_d1d2d3d4d5d6d7d8d9d10.size(); i < max_i; i++) {
            sum += list_d1d2d3d4d5d6d7d8d9d10.getQuick(i);
        }
        out.println("sum: " + sum + " in " + (currentTimeMillis() - time) + "ms");
    }
}

/*

Euler Problem 43 (http://projecteuler.net/index.php?section=problems&id=43): i've first tried to iterate over all numbers and when a pandigital was found, execute divisibility tests. But it is really long. So i splitted the problem in small parts to avoid working with big numbers, and i regularly used divisibility tests (http://mathworld.wolfram.com/DivisibilityTests.html) to validate numbers.

# d7d8d9 is divisible by 13
# d8d9d10 is divisible by 17

So we can find d7d8d9d10 by combining the list of numbers divisible by 13 or 17 having 2 digits in common

# d2d3d4 is divisible by 2
# d4d5d6 is divisible by 5
# d3d4d5 is divisible by 3

So we will combine all d2d3d4 numbers by d4d5d6 numbers having d4 in common, and i will check if the sum of digits d3d4d5 is divisible by 3.

Iterating over multiples is easy as we increment the counters by 2, 5, 13, 17, ... each time.

Also, d4d5d6 can't end with 0, because if d6 = 0, for d6d7d8 to be divisible by 11, d7 = d8: 011, 022, 033, ... Since we are looking for pandigital numbers, all digits must be different.
So to find all d4d5d6 we will increment by 10 each time.

Then we combine all d2d3d4d5d6 and d7d8d9d10 numbers found to form a 9-digit number and we check remaing divisibility tests 

# d5d6d7 is divisible by 7
# d6d7d8 is divisible by 11

Then we try for each values of d1 if d1d2d3d4d5d6d7d8d9d10 is pandigital, and ocmpute the sum

*/
