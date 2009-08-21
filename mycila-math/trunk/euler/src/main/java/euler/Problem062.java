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

import com.mycila.Digits;
import com.mycila.sequence.IntSequence;
import com.mycila.sequence.LongSequence;

import static java.lang.System.*;
import java.util.HashMap;
import java.util.Map;

/**
 * http://projecteuler.net/index.php?section=problems&id=62
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem062 {

    public static void main(String[] args) throws Exception {
        final long time = currentTimeMillis();
        final Digits digits = Digits.base(10);
        Map<IntSequence, LongSequence> map = new HashMap<IntSequence, LongSequence>();
        for (long i = 3, cube = i * i * i; i <= 2097151; i++, cube = i * i * i) {
            IntSequence signature = digits.signature(cube);
            LongSequence nums = map.get(signature);
            if (nums == null) map.put(signature, nums = new LongSequence(5));
            nums.addQuick(i);
            if (nums.size() == 5) {
                System.out.println(nums + " => " + (nums.first() * nums.first() * nums.first())
                        + " in " + (System.currentTimeMillis() - time) + "ms");
                break;
            }
        }
    }

}
