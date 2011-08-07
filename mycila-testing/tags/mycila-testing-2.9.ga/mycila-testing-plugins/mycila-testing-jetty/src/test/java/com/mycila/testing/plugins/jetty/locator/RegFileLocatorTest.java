/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mycila.testing.plugins.jetty.locator;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Iterables;
import com.mycila.testing.junit.MycilaJunitRunner;
import com.mycila.testing.plugin.annotation.ExpectException;

/**
 * Unit test of {@link RegFileLocator}.
 */
@RunWith(MycilaJunitRunner.class)
public class RegFileLocatorTest {
    
    @Test
    public final void testLocateExistsOne()
        throws FileNotFoundException
    {
        final Iterable<File> files = new RegFileLocator().locate(MATCH_ONE_REGEXP);
        Assert.assertTrue(Iterables.all(files, new ExistsFilePredicate()));
    }
    

    @Test
    @ExpectException(type = FileNotFoundException.class, message = "regexp '" + MATCH_MORE_THAN_ONE_REGEXP_QUOTED
            + "' matches more than one file : [./src/test/resources/file-a.txt, ./src/test/resources/file-b.txt]")
    public final void testLocateExistsMoreThanOne()
        throws FileNotFoundException
    {
        final Iterable<File> files = new RegFileLocator().locate(MATCH_MORE_THAN_ONE_REGEXP);
        Assert.assertTrue(Iterables.all(files, new ExistsFilePredicate()));
    }
    

    @Test
    @ExpectException(type = FileNotFoundException.class, message = "regexp '" + MATCH_LESS_THAN_ONE_REGEXP_QUOTED
            + "' matches less than one file : []")
    public final void testLocateExistsLessThanOne()
        throws FileNotFoundException
    {
        final Iterable<File> files = new RegFileLocator().locate(MATCH_LESS_THAN_ONE_REGEXP);
        Assert.assertTrue(Iterables.all(files, new ExistsFilePredicate()));
    }
    

    private static final String MATCH_ONE_REGEXP = "\\.\\/src\\/test\\/resources\\/file-a\\.txt";
    
    // private static final String MATCH_ONE_REGEXP_QUOTED = "\\\\.\\\\/src\\\\/test\\\\/resources\\\\/file-a\\\\.txt";
    
    private static final String MATCH_MORE_THAN_ONE_REGEXP = "\\.\\/src\\/test\\/resources\\/file-.*\\.txt";
    
    private static final String MATCH_MORE_THAN_ONE_REGEXP_QUOTED = "\\\\.\\\\/src\\\\/test\\\\/resources\\\\/file-.*\\\\.txt";
    
    private static final String MATCH_LESS_THAN_ONE_REGEXP = "\\.\\/src\\/test\\/resources\\/fil-.*\\.txt";
    
    private static final String MATCH_LESS_THAN_ONE_REGEXP_QUOTED = "\\\\.\\\\/src\\\\/test\\\\/resources\\\\/fil-.*\\\\.txt";
    
}
