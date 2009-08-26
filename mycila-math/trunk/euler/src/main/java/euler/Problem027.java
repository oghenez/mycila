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

import com.mycila.math.prime.Sieve;

/**
 * http://projecteuler.net/index.php?section=problems&id=27
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem027 {
    public static void main(String[] args) throws Exception {
        System.out.println(Sieve.to(1000));
    }
}

/*

Equation: p(x) = x^2 + ax + b

- |a| < 1000
- |b| < 1000
- p is prime

1. p(x) must generate prime numbers from x in [0, l]

p(0) = b => b must be prime

2. p(x) is prime

When x = b, p(b) = b^2 + ab + b = b (b + a + 1)

=> p(b) is not prime
=> limit l <= b

3. If p(x) is prime-generating for 0 <= x <= n, then so is p(n-x).
   See http://mathworld.wolfram.com/Prime-GeneratingPolynomial.html.

4. We know that p(x) = x^2 + x + 41 is prime genrating for x in [0, 39]

=> p(n-x) = (n-x)^2 + n-x + 41
   p(n-x) = n^2 + x^2 -2nx + n -x + 41
   p(n-x) = x^2 -x(2n+1) + [n^2 + n + 41]
   p(n-x) = x^2 -x(2n+1) + [p(n)]

=> p(n-x) = x^2 + Ax + B
   with A = -2n-1 and B = n^2 + n + 41

5. B < 1000

=> n^2 + n + 41 < 1000

racines: -1/2 +/- sqrt(1+4*959)/2
racines: -31.471 et 30.471

6. p(n-x) = x^2 -x(2n-1) + p(n) and p(n) is the generated prime for n

=> n <= 30
=> b <= 971 = p(30) = 30^2 + 30 + 41

7. We can see that p(X) = X^2 − 79X + 1601 = p(x-39), for x in [0, 79]
   So to increase the number of generated primes, we increase the factors.
   If we draw the two equations, we can see that composing creates new quadratic
   functions with a higher range of x generating primes.

   From 2), we saw that the limit l for x in [0, l] depends on the value of b.
   So if we want to generate the most primes as possible, we have to take l = b

   From 6), we saw that b <= 971 (this is the biggest value of b)

   So to be able to generate a lot of primes, we have to take l = b = 971
   So a = -1 - 2*30 = -61


So the quadraic equation (created by composing from euler's one) that will generate
the most of prime numbers such that |a| < 1000 and |b| < 1000 is:

p(x) = x^2 -61x + 971

-61 * 971 = -59231

*/
