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

/**
 * http://projecteuler.net/index.php?section=problems&id=40
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem040 {
    public static void main(String[] args) throws Exception {
        long time = currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; sb.length() <= 1000000; i++) sb.append(i);
        int prd = 1;
        for (int i = 1; i <= 1000000; i *= 10) {
            out.println(sb.charAt(i));
            prd *= (sb.charAt(i) - 48);
        }
        out.println(prd + " in " + (currentTimeMillis() - time) + "ms");
    }
}
