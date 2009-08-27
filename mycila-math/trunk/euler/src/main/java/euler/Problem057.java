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

import static java.lang.System.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=57
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem057 {

    public static void main(String[] args) throws Exception {
        long time = currentTimeMillis();

        BigInt p = BigInt.big(1);
        BigInt q = BigInt.big(1);
        final BigInt TWO = BigInt.big(2);
        int count = 0;
        for (long i = 1; i <= 1000; i++) {
            final BigInt nextP = p.add(q.multiply(TWO));
            q = p.add(q);
            p = nextP;
            if (p.length() > q.length()) {
                count++;
                System.out.println(p + "/" + q);
            }
        }

        out.println(count + " in " + (currentTimeMillis() - time) + "ms");
    }

}

/*

http://en.wikipedia.org/wiki/Square_root_of_2#Continued_fraction_representation

We define the fraction p/q for each iteration.

p(0)/q(0)=>1/1
p(1)/q(1)=>3/2
p(2)/q(2)=>7/5
p(3)/q(3)=>17/12
p(4)/q(4)=>41/29

p(n)/q(n)=>(p(n-1)+2*q(n-1))/(p(n-1)+q(n-1))

*/