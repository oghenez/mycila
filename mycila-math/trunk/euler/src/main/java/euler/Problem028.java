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

/**
 * http://projecteuler.net/index.php?section=problems&id=28
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem028 {
    public static void main(String[] args) throws Exception {
        long time = System.currentTimeMillis();
        System.out.println(" in " + (System.currentTimeMillis() - time) + "ms");
    }
}

/*

Problem 38 can also be solved without any code.

We call x the length of a square. 1 <= x <= 1001 and x is odd.

1. We call P(x) the 'perimeter' of a square: the number of numbers in a square.

P(x) = 4x - 4

For the square having x numbers in length, the total numbers in teh square is 4x-4

P(1) = 0
P(3) = 8
P(5) = 16
P(1001) = 4000

2. we call NE(x) the number in the north-east corner of the square.

NE(x) = x^2

3. we call NW(x) the number in the north-west corner of the square.

NW(x) = x^2 - x + 1

4. we call SE(x) the number in the south-east corner of the square.

SE(x) = x^2 - 3x + 3

3. we call SW(x) the number in the south-west corner of the square.

SW(x) = x^2 - 2x + 2

4. C(x) is the sum of the corners of the squere formed by x numbers per side.

C(x) = NE(x) + NW(x) + SE(x) + SW(x)
C(x) = 4x^2 - 6x + 6

5. thus, the sum S of the diagonals is

S = 1 + Sum(C(x)) for x from 3 to 1001, stepping by 2 (3, 5, 7, 9, ... 1001)
S = 1 + 4*Sum(x^2) - 6*Sum(x) + 6*Sum(1)

The number of numbers from 3 to 1001 is (1001-3)/2 + 1 = 500
1001 is the 501th odd number
See http://www.9math.com/book/sum-squares-first-n-odd-natural-numbers for the sum of odd squares

Sum(x^2) = 501*(2*501-1)*(2*501+1)/3 - 1 (we remove 1^2 because we add squares from 3 to 1001)

S = 1 + 4*(501*(2*501-1)*(2*501+1)/3-1) - 6*(3+1001)*500/2 + 6*500
S = 669171001

*/
