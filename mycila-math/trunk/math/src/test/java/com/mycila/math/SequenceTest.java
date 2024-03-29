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
package com.mycila.math;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author Mathieu Carbou
 */
public final class SequenceTest {

    @Test
    public void test_collatz() {
        assertEquals("collatz(0)", Sequence.collatz(0).toString(), "{}");
        assertEquals("collatz(1)", Sequence.collatz(1).toString(), "{1}");
        assertEquals("collatz(2)", Sequence.collatz(2).toString(), "{2, 1}");
        assertEquals("collatz(3)", Sequence.collatz(3).toString(), "{3, 10, 5, 16, 8, 4, 2, 1}");
        assertEquals("collatz(6)", Sequence.collatz(6).toString(), "{6, 3, 10, 5, 16, 8, 4, 2, 1}");
        assertEquals("collatz(123456789)", Sequence.collatz(123456789).toString(), "{123456789, 370370368, 185185184, 92592592, 46296296, 23148148, 11574074, 5787037, 17361112, 8680556, 4340278, 2170139, 6510418, 3255209, 9765628, 4882814, 2441407, 7324222, 3662111, 10986334, 5493167, 16479502, 8239751, 24719254, 12359627, 37078882, 18539441, 55618324, 27809162, 13904581, 41713744, 20856872, 10428436, 5214218, 2607109, 7821328, 3910664, 1955332, 977666, 488833, 1466500, 733250, 366625, 1099876, 549938, 274969, 824908, 412454, 206227, 618682, 309341, 928024, 464012, 232006, 116003, 348010, 174005, 522016, 261008, 130504, 65252, 32626, 16313, 48940, 24470, 12235, 36706, 18353, 55060, 27530, 13765, 41296, 20648, 10324, 5162, 2581, 7744, 3872, 1936, 968, 484, 242, 121, 364, 182, 91, 274, 137, 412, 206, 103, 310, 155, 466, 233, 700, 350, 175, 526, 263, 790, 395, 1186, 593, 1780, 890, 445, 1336, 668, 334, 167, 502, 251, 754, 377, 1132, 566, 283, 850, 425, 1276, 638, 319, 958, 479, 1438, 719, 2158, 1079, 3238, 1619, 4858, 2429, 7288, 3644, 1822, 911, 2734, 1367, 4102, 2051, 6154, 3077, 9232, 4616, 2308, 1154, 577, 1732, 866, 433, 1300, 650, 325, 976, 488, 244, 122, 61, 184, 92, 46, 23, 70, 35, 106, 53, 160, 80, 40, 20, 10, 5, 16, 8, 4, 2, 1}");
    }

}