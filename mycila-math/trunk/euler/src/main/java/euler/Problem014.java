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

import com.mycila.math.Sequence;
import com.mycila.math.list.LongSequence;

/**
 * http://projecteuler.net/index.php?section=problems&id=14
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem014 {
    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        LongSequence sequence = Sequence.collatz(999999L);
        for (long i = 999998; i > 1; i--) {
            LongSequence seq = Sequence.collatz(i);
            if (seq.size() > sequence.size()) sequence = seq;
        }
        System.out.println("Collatz(" + sequence.first() + ") has " + sequence.size() + " terms, in " + (System.currentTimeMillis() - time) + "ms\n" + sequence);
    }
}

// 837799
