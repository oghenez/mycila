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

import static java.lang.Math.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=25
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem025 {
    public static void main(String[] args) throws Exception {
        long time = System.currentTimeMillis();

        // We know that Fib(n) = Phi^n / √5 with Phi = (1 + √5) / 2 (Gold number)
        // If n is a number, the number of digits of n is: d(n) = floor(log10(n)) + 1
        // So we must found the first number n so that d(Fib(n)) >= 1000

        // => floor(log10(Phi^n / √5))) + 1 >= 1000
        // => floor( n * log10(Phi) - log10(5) / 2 ) >= 999
        // => n * log10(Phi) - log10(5) / 2 >= 999
        // => n >= (999 + log10(5) / 2) / log10(Phi);

        System.out.println(round((999.0 + log10(5.0) / 2.0) / log10((1.0 + sqrt(5)) / 2.0)) + " in " + (System.currentTimeMillis() - time) + "ms");
    }
}
// 4782
