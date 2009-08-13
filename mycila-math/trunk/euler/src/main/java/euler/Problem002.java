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
 * http://projecteuler.net/index.php?section=problems&id=2
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem002 {
    public static void main(String[] args) {
        // See http://fr.wikipedia.org/wiki/Suite_de_Fibonacci
        // Dans la suite de Fibo, F(n) est pair quand n est un multiple de 3
        // Donc il faut calculer la somme S des F(3i) de i=0 a i=X tant que F(3i) <= 4000000
        double sqrt5 = sqrt(5);
        double or = (1.0 + sqrt5) / 2.0;
        long sum = 0;
        for (long i = 0, fib; (fib = round(pow(or, i) / sqrt5)) <= 4000000; sum += fib, i += 3) ;
        out.println(sum);
    }
}
