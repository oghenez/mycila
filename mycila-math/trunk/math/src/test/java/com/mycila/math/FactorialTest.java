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
public final class FactorialTest {

    @Test
    public void test_lookup() {
        assertEquals(Factorial.lookup(0), 1);
        assertEquals(Factorial.lookup(1), 1);
        assertEquals(Factorial.lookup(2), 2);
        assertEquals(Factorial.lookup(10), 3628800);
        assertEquals(Factorial.lookup(20), 2432902008176640000L);
    }

    @Test
    public void test_falling() {
        assertEquals(Factorial.falling(0, 0), 1);
        assertEquals(Factorial.falling(1, 1), 1);
        assertEquals(Factorial.falling(2, 1), 2);
        assertEquals(Factorial.falling(20L, 10L), 670442572800L);
    }

}