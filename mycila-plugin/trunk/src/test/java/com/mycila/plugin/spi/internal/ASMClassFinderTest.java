/**
 * Copyright (C) 2010 Mathieu Carbou <mathieu.carbou@gmail.com>
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

package com.mycila.plugin.spi.internal;

import com.mycila.plugin.spi.DefaultLoader;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.net.URL;

import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class ASMClassFinderTest {

    @Test
    public void test_got_on() throws Exception {
        URL res = getClass().getResource("/com/mycila/plugin/spi/internal/ASMClassFinderTest.class");
        ASMClassFinder resolver = new ASMClassFinder(RunWith.class, new DefaultLoader());
        Class<?> c = resolver.resolve(res);
        assertEquals(ASMClassFinderTest.class, c);
    }

    @Test
    public void test_got_none() throws Exception {
        URL res = getClass().getResource("/com/mycila/plugin/spi/internal/ASMClassFinderTest.class");
        ASMClassFinder resolver = new ASMClassFinder(Ignore.class, new DefaultLoader());
        Class<?> c = resolver.resolve(res);
        assertNull(c);
    }

}
