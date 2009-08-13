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

/**
 * http://projecteuler.net/index.php?section=problems&id=5
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem005 {
    public static void main(String[] args) {
        // 2520 is the smallest number having [1, 2, 3, 4, 5, 6, 7, 8, 9, 10] as divisors
        // => prime decomposition gives: 2520 = 2^3 * 3^2 * 5 * 7, which includes all divisors

        // from 11 to 20:
        // - 11, 13, 17, 19 are primes => we will add them
        // - 12, 14, 15, 18, 20 are already divisors of 2520 (prime decomposition of these numbers is included in prime decomposition of 2520)
        // - 16 is 2^4, so we have to add only one 2

        // => our number N = 2^4 * 3^2 * 5 * 7 * 11 * 13 * 17 * 19 = 232792560
    }
}