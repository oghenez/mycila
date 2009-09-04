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
import static com.mycila.math.number.BigInt.*;
import static org.junit.Assert.*;

import static java.lang.System.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=48
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem048 {
    public static void main(String[] args) throws Exception {
        long time = currentTimeMillis();
        BigInt sum = ZERO;
        for (int n = 1; n <= 1000; n++) sum = sum.modAdd(big(n).modPow(n, 10000000000L), big(10000000000L));
        out.println(sum + " in " + (currentTimeMillis() - time) + "ms");
        assertEquals(big(9110846700L), sum);
    }

}

// 9110846700 in 88ms
