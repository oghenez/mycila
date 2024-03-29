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

import com.mycila.math.Factorial;

/**
 * @author Mathieu Carbou
 */
public final class Combinations {

    private Combinations() {
    }

    /**
     * Compute the Nth <a href="http://en.wikipedia.org/wiki/Catalan_numbers">Catalan number</a>
     *
     * @param index index
     * @return Catalan(i)
     */
    public static long catalan(int index) {
        return binomial(index << 1, index) / (index + 1);
    }

    public static long binomial(int n, int r) {
        return Factorial.falling(n, r) / Factorial.lookup(r);
    }

}
