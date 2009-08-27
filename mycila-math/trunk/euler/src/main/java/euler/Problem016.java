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

/**
 * http://projecteuler.net/index.php?section=problems&id=16
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem016 {
    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        BigInt n = BigInt.big(2).pow(1000);
        char[] digits = n.toString().toCharArray();
        long sum = 0;
        for (int i = 0; i < digits.length; i++) sum += digits[i] - 48;
        System.out.println(sum + " in " + (System.currentTimeMillis() - time) + "ms");
    }
}