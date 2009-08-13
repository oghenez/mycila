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
import static java.lang.System.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=10
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem010 {
    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        long sump = 2L;
        for (long i = 3L; i < 2000000L; i += 2L) if (isPrime(i)) sump += i;
        out.println(sump); // 142913828922
        System.out.println(System.currentTimeMillis() - time);
    }

    private static boolean isPrime(long n) {
        if (n == 2L) return true;
        else if (n <= 1L || (n & 1L) == 0L) return false;
        for (long p = 3L, sqrt = (long) sqrt(n); p <= sqrt; p += 2L) if (n % p == 0L) return false;
        return true;
    }

}
