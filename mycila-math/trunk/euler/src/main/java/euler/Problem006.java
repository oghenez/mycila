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
 * http://projecteuler.net/index.php?section=problems&id=6
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem006 {
    public static void main(String[] args) {
        // SOLUTION 1:
        long sum = (100 * (100 + 1) / 2) * (100 * (100 + 1) / 2);
        for (int i = 1; i <= 100; i++) sum -= i * i;
        out.println(sum); // 25164150

        // SOLUTION 2:
        // [(1 + 2 + ... + 99 + 100) * (1 + 2 + ... + 99 + 100)] - [1*1 + 2*2 + ... + 99*99 + 100*100]
        // we can avoid all the computation of (1*1 + 2*2 + ... + 99*99 + 100*100)
        // since it is already included in the first Expression
        sum = 0;
        for (int i = 1; i <= 100; i++) for (int j = 1; j <= 100; j++) if (i != j) sum += i * j;
        out.println(sum);
    }
}