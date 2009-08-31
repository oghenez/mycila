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
import java.util.LinkedList;
import java.util.List;

/**
 * http://projecteuler.net/index.php?section=problems&id=3
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem003 {
    public static void main(String[] args) {

        System.out.println("SOLUTION 1: Using Long:");

        out.println("-1: " + isPrime(-1));
        out.println("0: " + isPrime(0));
        out.println("1: " + isPrime(1));
        out.println("2: " + isPrime(2));
        out.println("3: " + isPrime(3));
        out.println("4: " + isPrime(4));
        out.println("5: " + isPrime(5));
        out.println("6: " + isPrime(6));
        out.println("600851475143: " + isPrime(600851475143L));

        out.println("factor(-1): " + factor(-1));
        out.println("factor(0): " + factor(0));
        out.println("factor(1): " + factor(1));
        out.println("factor(2): " + factor(2));
        out.println("factor(3): " + factor(3));
        out.println("factor(4): " + factor(4));
        out.println("factor(5): " + factor(5));
        out.println("factor(6): " + factor(6));
        out.println("factor(600851475143): " + factor(600851475143L));
    }

    private static boolean isPrime(long n) {
        if (n == 2L) return true;
        else if (n <= 1L || (n & 1L) == 0L) return false;
        for (long p = 3L, sqrt = (long) sqrt(n); p <= sqrt; p += 2L) if (n % p == 0L) return false;
        return true;
    }

    public static List<Long> factor(long n) {
        List<Long> factors = new LinkedList<Long>();
        if (n < 0L) {
            n = abs(n);
        }
        if (n <= 1L) {
            return factors;
        }
        while ((n & 1L) == 0L) {
            n >>>= 1L;
            factors.add(2L);
        }
        for (long p = 3L; p <= n; p += 2)
            if (isPrime(p)) while (n % p == 0L) {
                n /= p;
                factors.add(p);
            }
        return factors;
    }

}