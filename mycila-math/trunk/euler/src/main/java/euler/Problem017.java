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

import com.mycila.Format;

/**
 * http://projecteuler.net/index.php?section=problems&id=17
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem017 {
    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 1000; i++) sb.append(Format.asWords(i)).append("\n");
        System.out.println(sb.toString().replaceAll("\\s", "").length() + " in " + (System.currentTimeMillis() - time) + "ms");
    }
}

// 21124
