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

import static java.util.regex.Matcher.quoteReplacement;
import static org.apache.commons.io.FileUtils.listFiles;
import static org.apache.commons.io.filefilter.FileFilterUtils.trueFileFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;

import org.apache.commons.io.filefilter.IOFileFilter;

class RegFileLocator
        implements FileLocator {

    /**
     * {@inheritDoc}
     * 
     * @see com.mycila.testing.plugins.jetty.FileLocator#locate(java.lang.String)
     */
    public File locate(
            final String path)
        throws FileNotFoundException
    {
        final Collection<File> files = listFiles(new File("."), new RegExpFileFilter(path), trueFileFilter());
        if (files.size() < 1) {
            throw new FileNotFoundException("regexp '" + quoteReplacement(path) + "' matches less than one file : "
                    + files);
        }
        else if (files.size() > 1) {
            throw new FileNotFoundException("regexp '" + quoteReplacement(path) + "' matches more than one file : "
                    + files);
        }

        final File file = files.iterator().next();
        return file;
    }

    static class RegExpFileFilter
            implements IOFileFilter {

        RegExpFileFilter(
                final String regexp)
        {
            this.regexp = regexp;
        }


        public boolean accept(
                final File dir,
                final String name)
        {
            return this.accept(new File(dir, name));
        }


        public boolean accept(
                final File file)
        {
            final boolean matches = file.getPath().matches(this.regexp);
            return matches;
        }

        private final String regexp;

    }

}
