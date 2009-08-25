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

import java.util.HashMap;
import java.util.Map;

/**
 * http://projecteuler.net/index.php?section=problems&id=21
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem021 {
    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        Map<Integer, Integer> sumOfDivisors = new HashMap<Integer, Integer>();
        for (int n = 2; n < 10000; n++) {
            int sum = 0;
            for (int d = 1; d < n; d++) if (n % d == 0) sum += d;
            sumOfDivisors.put(n, sum);
        }
        System.out.println(sumOfDivisors);
        System.out.println(Divisors.sum(10000));
        int sumOfAmicables = 0;
        for (Map.Entry<Integer, Integer> entry : sumOfDivisors.entrySet()) {
            if (entry.getKey().equals(entry.getValue())) {
                System.out.println("Perfect number: " + entry.getKey());
                continue;
            }
            Integer sum = sumOfDivisors.get(entry.getValue());
            if (sum != null && sum.equals(entry.getKey())) {
                System.out.println("Amicable number: " + entry.getKey());
                sumOfAmicables += entry.getKey();
            }
        }
        System.out.println(sumOfAmicables + " in " + (System.currentTimeMillis() - time) + "ms");
    }
}
