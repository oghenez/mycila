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
import com.mycila.math.list.IntSequence;
import static euler.Problem061.POLYGON.*;

import static java.lang.System.*;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * http://projecteuler.net/index.php?section=problems&id=61
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem061 {

    public static void main(String[] args) throws Exception {
        final long time = currentTimeMillis();

        for (POLYGON polygon : values())
            System.out.println(polygon.describe());

        Map<POLYGON, Integer> pass = new LinkedHashMap<POLYGON, Integer>();

        for (int n : OCTAGONAL.sequence) {
            pass.put(OCTAGONAL, n);
            if (findNextNumber(n % 100, pass)) break;
        }

        int sum = 0;
        for (Map.Entry<POLYGON, Integer> entry : pass.entrySet()) {
            sum += entry.getValue();
            System.out.println(entry.getKey().name() + ": " + entry.getValue());
        }

        System.out.println(sum + " in " + (System.currentTimeMillis() - time) + "ms");
    }

    private static boolean findNextNumber(int twoDgts, Map<POLYGON, Integer> pass) {
        if (pass.size() == 6) {
            if (twoDgts == pass.get(OCTAGONAL) / 100) return true;
        } else {
            Set<Map.Entry<POLYGON, IntSequence>> possibleNext = numbersStartingWith(twoDgts, pass);
            for (Map.Entry<POLYGON, IntSequence> entry : possibleNext) {
                for (int n : entry.getValue()) {
                    pass.put(entry.getKey(), n);
                    if (findNextNumber(n % 100, pass)) return true;
                }
                pass.remove(entry.getKey());
            }
        }
        return false;
    }

    private static Set<Map.Entry<POLYGON, IntSequence>> numbersStartingWith(int twoDgts, Map<POLYGON, Integer> pass) {
        Map<POLYGON, IntSequence> map = new LinkedHashMap<POLYGON, IntSequence>();
        for (POLYGON polygon : EnumSet.complementOf(EnumSet.copyOf(pass.keySet()))) {
            IntSequence nums = new IntSequence();
            for (int n : polygon.sequence) if (twoDgts == n / 100) nums.add(n);
            if (!nums.isEmpty()) map.put(polygon, nums);
        }
        return map.entrySet();
    }

    static enum POLYGON {

        OCTAGONAL(new Gen() {
            @Override
            public long gen(int index) {
                return Polygon.octagonal(index);
            }
        }),

        TRIANGLE(new Gen() {
            @Override
            public long gen(int index) {
                return Polygon.triangle(index);
            }
        }),

        SQUARE(new Gen() {
            @Override
            public long gen(int index) {
                return Polygon.square(index);
            }
        }),

        PENTAGONAL(new Gen() {
            @Override
            public long gen(int index) {
                return Polygon.pentagonal(index);
            }
        }),

        HEXAGONAL(new Gen() {
            @Override
            public long gen(int index) {
                return Polygon.hexagonal(index);
            }
        }),

        HEPTAGONAL(new Gen() {
            @Override
            public long gen(int index) {
                return Polygon.heptagonal(index);
            }
        }),;

        private final IntSequence sequence = new IntSequence();

        private POLYGON(Gen gen) {
            for (int i = 1; i < 9999; i++) {
                int n = (int) gen.gen(i);
                if (n >= 1010 && n <= 9999 && n % 100 > 9)
                    sequence.add(n);
            }
            sequence.sort();
        }

        public String describe() {
            return sequence.size() + " " + name().toLowerCase() + "s: " + sequence;
        }
    }

    private static interface Gen {
        long gen(int index);
    }

}

/*

From the definitions, we can see that the most progressing serie is for octagonal numbers.
So between 1000 and 9999 there will be less octagonal numbers than other numbers.
Also we can limit the search to numbers ending with 2 digits greater than 9, to be sure they can be the 2 starting digits of another number.

We can easily enumerate them:

30 octagonals
88 triangles
53 squares
47 pentagonals
44 hexagonals
40 heptagonals

*/
