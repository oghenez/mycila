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
import java.util.Set;
import java.util.TreeSet;

/**
 * http://projecteuler.net/index.php?section=problems&id=4
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem004 {
    public static void main(String[] args) {

        final Palindroms palindrom = Palindroms.base(10);

        Set<Integer> palindroms = new TreeSet<Integer>();
        for (int i = 999; i >= 100; i--) for (int j = i, prod = i * j; j >= 100; j--, prod = i * j) if (prod % 11 == 0 && ("" + prod).equals(new StringBuilder("" + prod).reverse().toString())) palindroms.add(prod);
        out.println(palindroms);

        palindroms.clear();
        for (int i = 999; i >= 100; i--) for (int j = i, prod = i * j; j >= 100; j--, prod = i * j) if (prod % 11 == 0 && palindrom.isPalindromic(prod)) palindroms.add(prod);
        out.println(palindroms);
    }
}