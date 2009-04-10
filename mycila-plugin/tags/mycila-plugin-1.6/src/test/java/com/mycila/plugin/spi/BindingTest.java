/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
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

package com.mycila.plugin.spi;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class BindingTest {

    @Test
    public void test_toString() {
        assertEquals(new Binding("aa").toString(), "aa");
    }

    @Test
    public void test_hash() {
        assertEquals(new Binding("aa").hashCode(), "aa".hashCode());
    }

    @Test
    public void test_equals() {
        assertFalse(new Binding("aa").equals(null));
        assertFalse(new Binding("aa").equals(3));
        assertEquals(new Binding("aa"), new Binding("aa"));
        assertEquals("aa", new Binding("aa"));
    }
}