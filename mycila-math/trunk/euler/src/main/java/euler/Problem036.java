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

import com.mycila.Palindroms;

import static java.lang.System.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=36
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem036 {
    public static void main(String[] args) throws Exception {
        final long time = currentTimeMillis();
        final Palindroms palindroms10 = Palindroms.base(10);
        final Palindroms palindroms2 = Palindroms.base(2);
        int sum = 0;
        for (int i = 1; i < 1000000; i += 2) {
            if (palindroms10.isPalindromic(i) && palindroms2.isPalindromic(i)) sum += i;
        }
        out.println(sum + " in " + (currentTimeMillis() - time) + "ms");
    }
}

/*

Numbers must be palindromic in base 2 and 10. It means the numbers cannot end with 0. In base 2, it means we can skip all odd numbers.

*/