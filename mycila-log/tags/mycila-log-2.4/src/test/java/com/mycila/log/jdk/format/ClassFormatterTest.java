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
package com.mycila.log.jdk.format;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ClassFormatterTest {
    @Test
    public void test_stripped() throws Exception {
        ClassFormatter classFormatter = new ClassFormatter();
        assertEquals(classFormatter.stripped(""), "");
        assertEquals(classFormatter.stripped("AppMain"), "AppMain");
        assertEquals(classFormatter.stripped(".AppMain"), "AppMain");
        assertEquals(classFormatter.stripped("a.AppMain"), "a.AppMain");
        assertEquals(classFormatter.stripped(".a.AppMain"), "a.AppMain");
        assertEquals(classFormatter.stripped("a.b.AppMain"), "a.b.AppMain");
        assertEquals(classFormatter.stripped(".a.b.AppMain"), "a.b.AppMain");
        assertEquals(classFormatter.stripped("a.b.c.AppMain"), "b.c.AppMain");
        assertEquals(classFormatter.stripped(".a.b.c.AppMain"), "b.c.AppMain");
    }
}
