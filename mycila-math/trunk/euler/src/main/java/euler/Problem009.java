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

import static java.lang.Math.*;
import static java.lang.System.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=9
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem009 {
    public static void main(String[] args) {
        for (int a = 2; a < 1000; a++) for (int b = a + 1; b < 1000; b++) if (1000.0 - a - b == sqrt(a * a + b * b)) out.println(a + "," + b + "," + (1000 - a - b) + " : " + a * b * (1000 - a - b));
    }
}

// 200,375,425 : 31875000
