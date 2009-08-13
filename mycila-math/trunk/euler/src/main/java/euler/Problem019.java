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

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

/**
 * http://projecteuler.net/index.php?section=problems&id=19
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem019 {
    public static void main(String[] args) throws Exception {
        long time = System.currentTimeMillis();
        int sum = 0;
        for (LocalDate date = new LocalDate(1901, 1, 1), max = new LocalDate(2000, 12, 31); date.isBefore(max); date = date.plusMonths(1))
            if (date.getDayOfWeek() == DateTimeConstants.SUNDAY)
                sum++;
        System.out.println(sum + " in " + (System.currentTimeMillis() - time) + "ms");
    }
}
