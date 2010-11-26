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
import java.io.FileNotFoundException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.mycila.testing.junit.MycilaJunitRunner;
import com.mycila.testing.plugin.annotation.ExpectException;

/**
 * Unit test of {@link SysFileLocator}.
 */
@RunWith(MycilaJunitRunner.class)
public class SysFileLocatorTest {

    @Test
    @ExpectException(type = FileNotFoundException.class, message = "property '" + WAR_PATH_PROPERTY
            + "' does not locate any file")
    public final void testLocateFailed()
        throws FileNotFoundException
    {
        final File file = new SysFileLocator().locate(WAR_PATH_PROPERTY);
        Assert.assertFalse(file.isFile());
    }


    @Test
    public final void testLocateSucceed()
        throws FileNotFoundException
    {
        System.setProperty(WAR_PATH_PROPERTY, "src/test/resources/file-a.txt");
        final File file = new SysFileLocator().locate(WAR_PATH_PROPERTY);
        Assert.assertTrue(file.isFile());
    }


    @Test
    public final void testLocateFailedWithNonExistentFile()
        throws FileNotFoundException
    {
        System.setProperty(WAR_PATH_PROPERTY, "src/test/resources/file.txt");
        final File file = new SysFileLocator().locate(WAR_PATH_PROPERTY);
        Assert.assertFalse(file.isFile());
    }

    private static final String WAR_PATH_PROPERTY = "warPath";

}
