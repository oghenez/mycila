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

import java.math.BigInteger;

/**
 * http://projecteuler.net/index.php?section=problems&id=20
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem020 {
    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        BigInteger res = BigInteger.valueOf(1);
        for (int i = 2; i <= 100; i++) res = res.multiply(BigInteger.valueOf(i));
        char[] digits = res.toString().toCharArray();
        long sum = 0;
        for (char digit : digits) sum += digit - 48;
        System.out.println(res + " : " + sum + " in " + (System.currentTimeMillis() - time) + "ms");
    }
}