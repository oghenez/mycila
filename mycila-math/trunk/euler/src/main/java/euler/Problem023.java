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

import com.mycila.math.Divisors;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * http://projecteuler.net/index.php?section=problems&id=23
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem023 {
    public static void main(String[] args) throws FileNotFoundException {
        long time = System.currentTimeMillis();

        // from http://mathworld.wolfram.com/AbundantNumber.html
        // "Every number greater than 20161 can be expressed as a sum of two abundant numbers."
        final int max = 20161;

        // find all abundant numbers
        List<Integer> abundants = new ArrayList<Integer>();
        for (int n = 2; n <= max; n++)
            if (Divisors.isAbundant(n))
                abundants.add(n);

        for (int i = 1; i <= abundants.size(); i++)
            System.out.println(i + ": " + abundants.get(i - 1));

        // mark all numbers which are the sums of abundants
        BitSet sumOfAbundants = new BitSet(max + 1);
        for (int i = 0; i < abundants.size(); i++)
            for (int j = 0, s; j <= i && (s = abundants.get(i) + abundants.get(j)) < max; j++)
                sumOfAbundants.set(s);

        // summarize not composed abundant numbers
        int sum = 0;
        for (int i = 1; i <= max; i++)
            if (!sumOfAbundants.get(i))
                sum += i;

        System.out.println(sum + " in " + (System.currentTimeMillis() - time) + "ms");
    }

}
// 4179871