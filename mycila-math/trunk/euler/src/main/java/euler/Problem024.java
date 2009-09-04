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

import com.mycila.combination.Factoradic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=24
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem024 {
    public static void main(String[] args) throws Exception {
        long time = System.currentTimeMillis();

        // http://en.wikipedia.org/wiki/Factoradic
        // We use the Factoradic numbering system to find the solution faster
        // Our base is: 0 1 2 3 4 5 6 7 8 9
        // We have to find F(999999).
        // F(0) = 0x9! + 0x8! + 0x7! + 0x6! + 0x5! + 0x4! + 0x3! + 0x2! + 0x1! + 0x0! (=> 0 1 2 3 4 5 6 7 8 9)
        // F(1) = 0x9! + 0x8! + 0x7! + 0x6! + 0x5! + 0x4! + 0x3! + 0x2! + 1x1! + 0x0! (=> 0 1 2 3 4 5 6 7 9 8)
        // F(1440) = 0x9! + 0x8! + 0x7! + 2x6! + 0x5! + 0x4! + 0x3! + 0x2! + 0x1! + 0x0! (=> 0 1 2 3 6 4 5 7 9 8)

        final int BASE = 10;

        // compute the base (9!, 8!, 7!, ...)
        int[] base = new int[BASE];
        for (int i = BASE - 1; i >= 0; i--) base[BASE - 1 - i] = fact(i);
        System.out.println(Arrays.toString(base));

        // find the digits of the Factoradic number for 999999
        int[] digits = new int[BASE];
        for (int i = BASE - 1, n = 999999; i >= 0; i--) {
            int remain = n % base[BASE - 1 - i];
            digits[BASE - 1 - i] = (n - remain) / base[BASE - 1 - i];
            n = remain;
        }
        System.out.println("F(999999): " + Arrays.toString(digits));

        // now, that we have the index of the permuted numbers, we display these numbers:
        List<Integer> numbers = new ArrayList<Integer>(BASE);
        for (int i = 0; i < BASE; i++) numbers.add(i);
        StringBuilder sb = new StringBuilder();
        for (int digit : digits) sb.append(numbers.remove(digit));

        System.out.println("1000000th permutation is " + sb + " in " + (System.currentTimeMillis() - time) + "ms");
        System.out.println(Factoradic.base(10).permutations(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).get(1000000 - 1));

        assertEquals("2783915460", sb.toString());
        assertEquals("[2, 7, 8, 3, 9, 1, 5, 4, 6, 0]", Factoradic.base(10).permutations(0, 1, 2, 3, 4, 5, 6, 7, 8, 9).get(1000000 - 1).toString());
    }

    private static int fact(int n) {
        int res = n > 1 ? n : 1;
        while (n-- > 1) res *= n;
        return res;
    }
}
