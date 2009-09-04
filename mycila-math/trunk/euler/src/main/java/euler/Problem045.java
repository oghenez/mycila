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

import static java.lang.System.*;

import static org.junit.Assert.*;

/**
 * http://projecteuler.net/index.php?section=problems&id=45
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Problem045 {
    public static void main(String[] args) throws Exception {
        final long time = currentTimeMillis();
        for (int i = 286; ; i++) {
            final long ti = Polygon.triangle(i);
            final long n = Polygon.isHexagonal(ti);
            final long m = Polygon.isPentagonal(ti);
            if (n != -1 && m != -1) {
                out.println("T(" + i + ")=P(" + m + ")=H(" + n + ")=" + ti);
                out.println("T(" + i + ")=" + Polygon.triangle(i));
                out.println("P(" + m + ")=" + Polygon.pentagonal(m));
                out.println("H(" + n + ")=" + Polygon.hexagonal(n));
                out.println(ti + " in " + (currentTimeMillis() - time) + "ms");
                assertEquals(1533776805L, ti);
                break;
            }
        }
    }
}
