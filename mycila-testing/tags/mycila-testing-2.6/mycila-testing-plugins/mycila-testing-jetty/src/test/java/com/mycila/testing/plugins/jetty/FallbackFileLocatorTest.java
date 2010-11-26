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

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Test;

/**
 * Unit test of {@link FallbackFileLocator}.
 */
public class FallbackFileLocatorTest {

    @Test
    public final void testLocateNoFallback()
        throws Exception
    {
        final FileLocator locator = mock(FileLocator.class);
        final FileLocator fallbackLocator = mock(FileLocator.class);
        when(locator.locate("some-file")).thenReturn(new File("."));

        new FallbackFileLocator(locator, fallbackLocator).locate("some-file");

        verify(fallbackLocator, never()).locate(anyString());
    }


    @Test
    public final void testLocateFallback()
        throws Exception
    {
        final FileLocator locator = mock(FileLocator.class);
        final FileLocator fallbackLocator = mock(FileLocator.class);
        when(locator.locate("some-file")).thenReturn(new File("non-existent-file"));

        new FallbackFileLocator(locator, fallbackLocator).locate("some-file");

        verify(fallbackLocator).locate("some-file");
    }

}
