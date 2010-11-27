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

package com.mycila.testing.plugins.jetty;

import java.io.File;
import java.io.FileNotFoundException;

public class FallbackFileLocator
        implements FileLocator {

    public FallbackFileLocator(
            final FileLocator locator,
            final FileLocator fallbackLocator)
    {
        this.locator = locator;
        this.fallbackLocator = fallbackLocator;
    }


    public File locate(
            final String path)
        throws FileNotFoundException
    {
        File file = this.locator.locate(path);

        if ((file == null) || !file.exists()) {
            file = this.fallbackLocator.locate(path);
        }

        return file;
    }

    private final FileLocator locator;

    private final FileLocator fallbackLocator;

}
