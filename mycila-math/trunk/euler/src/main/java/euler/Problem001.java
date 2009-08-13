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

import static java.lang.System.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=1
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem001 {
    public static void main(String[] args) {
        // SOLUTION 1: modulos
        int sum = 0;
        for (int i = 0; i < 1000; i++) if (i % 3 == 0 || i % 5 == 0) sum += i;
        out.println(sum);

        // SOLUTION 2: 
        // sum of consecutive numbers: n(f+l)/2 (n = number of terms, f = first, l = last)
        // between 1 and 999, there are (int) 999 / 3 multiples of 3
        out.println(3 * (int) (999 / 3) * ((int) (999 / 3) + 1) / 2
                + 5 * (int) (999 / 5) * ((int) (999 / 5) + 1) / 2
                - 15 * (int) (999 / 15) * ((int) (999 / 15) + 1) / 2);
    }
}
