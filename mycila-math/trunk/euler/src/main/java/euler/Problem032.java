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
import com.mycila.sequence.IntProcedure;

import java.util.BitSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * http://projecteuler.net/index.php?section=problems&id=32
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem032 {
    public static void main(String[] args) throws Exception {
        final long time = System.currentTimeMillis();
        final Digits digits = Digits.base(10);
        final Set<Integer> products = new TreeSet<Integer>();
        final BitSet bitSet = new BitSet(9);
        // 1-digit number by a 4-digit number
        for (int i = 1234; i <= 9876; i++) {
            for (int j = 1; j <= 8; j++) {
                int prd = i * j;
                if (prd >= 1234 && prd <= 9876) {
                    bitSet.clear();
                    if (digits.each(i * 100000 + j * 10000 + prd, new IntProcedure() {
                        public boolean execute(int value) {
                            if (value == 0) return false;
                            if (bitSet.get(value - 1)) return false;
                            bitSet.set(value - 1);
                            return true;
                        }
                    })) products.add(prd);
                }
            }
        }
        // 2-digit number by a 3-digit number
        for (int i = 123; i <= 987; i++) {
            for (int j = 12; j <= 98; j++) {
                int prd = i * j;
                if (prd >= 1234 && prd <= 9876) {
                    bitSet.clear();
                    if (digits.each(i * 1000000 + j * 10000 + prd, new IntProcedure() {
                        public boolean execute(int value) {
                            if (value == 0) return false;
                            if (bitSet.get(value - 1)) return false;
                            bitSet.set(value - 1);
                            return true;
                        }
                    })) products.add(prd);
                }
            }
        }
        System.out.println(products);
        int sum = 0;
        for (Integer product : products) sum += product;
        System.out.println(sum + " in " + (System.currentTimeMillis() - time) + "ms");
    }
}

/*

We try to find which combination can produce 9 digits from 1 to 9.

9 x 8 = 72 (4 digits)
9 x 87 = 783 (6 digits)
9 x 876 = 7884 (8 digits)
9 x 1234 = 11106 (10 digits)
8 x 1234 = 9872 (9 digits)

So a digit from 1 to 8 multiplied by a 4-digit number can give a 4-digit number

98 x 76 = 7448 (8 digits)
98 x 765 = 74970 (10 digits)
12 x 345 = 4140 (9 digits)

So we must also check 2-digit numbers multiplied by 3-digit numbers

outputs: 45228 in 91ms

*/
