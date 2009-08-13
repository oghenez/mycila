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

import static java.lang.System.*;
import java.util.Scanner;

/**
 * http://projecteuler.net/index.php?section=problems&id=42
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem042 {
    public static void main(String[] args) throws Exception {
        final long time = currentTimeMillis();
        int count = 0;
        for (Scanner scanner = new Scanner(Problem042.class.getResourceAsStream("/words.txt")).useDelimiter(","); scanner.hasNext();) {
            String word = scanner.next();
            int v = value(word);
            double n1 = -0.5 + Math.sqrt(0.25 + 2 * v);
            if (n1 == (int) n1) {
                count++;
                System.out.println(word + ": t(" + (int) n1 + ")=" + v);
            }
        }
        out.println(count + " in " + (currentTimeMillis() - time) + "ms");
    }

    private static int value(String s) {
        s = s.toUpperCase();
        int v = 0;
        for (int i = 0, max = s.length(); i < max; i++) v += s.charAt(i) - 64;
        return v;
    }
}

/*

Problem 42:

For each word in the list, we get it's word value v. We then check if there is a solution for the equation:

v=t_n=0.5*n*(n+1)

The positive integer solutions (which have no fractional part) correspond to t_n, the Nth triangle number.

0.5*n^2+0.5*n-v=0

solutions:

n1=(-0.5+sqrt{0.5^2+4*0.5*v})/2*0.5
n2=(-0.5-sqrt{0.5^2+4*0.5*v})/2*0.5

n1=-0.5+sqrt{0.25+2*v}
n2=-0.5-sqrt{0.25+2*v}

We will only comput n1 since n2 will be negative.

*/