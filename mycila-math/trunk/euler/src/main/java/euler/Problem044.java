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

import com.mycila.math.Polygon;
import com.mycila.math.sequence.IntSequence;

import static java.lang.System.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=44
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem044 {
    public static void main(String[] args) throws Exception {
        final long time = currentTimeMillis();
        final IntSequence pentagonals = new IntSequence();
        pentagonals.add(1);
        for (int k = 2; ; k++) {
            final int pk = pentagonals.getQuick(k - 2) + 3 * k - 2;
            pentagonals.add(pk);
            for (int j = k - 2; j >= 0; j--) {
                final int d = pk - pentagonals.getQuick(j);
                if (pentagonals.binarySearch(d) > 0 && Polygon.isPentagonal(pk + pentagonals.getQuick(j)) != -1) {
                    out.println(d + " in " + (currentTimeMillis() - time) + "ms");
                    return;
                }
            }
        }
    }
}

/*

P(n)=n(3n-1)/2

The pentagonal sequence can be created with the following recursive definition:

P(1) = 1
P(n) = P(n-1) + 3n - 2

*/