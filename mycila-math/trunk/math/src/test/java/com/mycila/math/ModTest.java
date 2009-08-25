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
public final class ModTest {

    @Test
    public void test_add() {
        assertEquals(Mod.add(131, 145, 100), 76);
        assertEquals(Mod.add(Integer.MAX_VALUE, Integer.MAX_VALUE, 100), 94);
    }

    @Test
    public void test_mult() {
        assertEquals(Mod.multiply(131, 145, 100), 95);
        assertEquals(Mod.multiply(Integer.MAX_VALUE, Integer.MAX_VALUE, 100), 9);
    }

    @Test
    public void test_pow() {
        assertEquals(Mod.pow(131, 3, 100), 91);
        assertEquals(Mod.pow(Integer.MAX_VALUE, 2, 100), 9);
    }

}