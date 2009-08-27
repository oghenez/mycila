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

import com.mycila.math.number.BigInteger;

import java.util.Set;
import java.util.TreeSet;

/**
 * http://projecteuler.net/index.php?section=problems&id=29
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem029 {
    public static void main(String[] args) throws Exception {
        long time = System.currentTimeMillis();
        Set<BigInteger> numbers = new TreeSet<BigInteger>();
        for (int i = 2; i <= 100; i++)
            for (int j = 2; j <= 100; j++)
                numbers.add(BigInteger.big(i).pow(j));
        System.out.println(numbers.size() + " in " + (System.currentTimeMillis() - time) + "ms");
    }
}