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

package com.mycila.testing.plugins.jetty;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test of {@link PathFileLocator}.
 */
public class PathFileLocatorTest {

    @Test
    public final void testLocateRelativeFound()
    {
        final File file = new PathFileLocator().locate("src/test/resources/file-a.txt");
        Assert.assertTrue(file.isFile());
    }


    @Test
    public final void testLocateAbsoluteNotFound()
    {
        final File file = new PathFileLocator().locate("/src/test/resources/file-a.txt");
        Assert.assertFalse(file.isFile());
    }

}
