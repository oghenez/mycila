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

import com.mycila.math.Pythagore;
import com.mycila.math.triplet.IntTriplet;

import static java.lang.System.*;
import java.util.List;

import static org.junit.Assert.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=39
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem039 {
    public static void main(String[] args) throws Exception {
        final long time = currentTimeMillis();
        List<IntTriplet> max = Pythagore.triplet(1);
        for (int n = 2; n <= 1000; n++) {
            List<IntTriplet> triplets = Pythagore.triplet(n);
            if (triplets.size() > max.size()) max = triplets;
        }
        out.println(max.get(0).sum() + " : " + max + " in " + (currentTimeMillis() - time) + "ms");
        assertEquals(840, max.get(0).sum());
        assertEquals("[(210,280,350), (140,336,364), (315,168,357), (252,240,348), (350,120,370), (390,56,394), (399,40,401)]", max.toString());
    }
}
