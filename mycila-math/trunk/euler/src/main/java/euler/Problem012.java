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

import com.mycila.Decomposition;

import static java.lang.System.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=12
 * <p/>
 * http://home.att.net/~numericana/answer/numbers.htm#divisors
 * The number of divisors of a number N is (a+1)(b+1)(c+1)
 * where N = A^a + B^b + C^c
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem012 {
    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        // We know that the first number to have over 500 divisors will be less or equals than
        // the number composed of the first 501 primes.
        // So all the divisors of the triangle number will be composed of some of these 501 primes
        for (int i = 1, triangle = 1; ; i++, triangle = i * (1 + i) / 2) {
            if (Decomposition.of(triangle).divisorCount() > 500) {
                out.println(triangle);
                break;
            }
        }
        System.out.println(System.currentTimeMillis() - time + "ms");
    }
}

// output:
// 76576500
// 124ms

