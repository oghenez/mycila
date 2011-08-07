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

import static com.mycila.testing.plugins.jetty.locator.StrategyFileLocator.Strategy.findStrategy;
import static java.util.EnumSet.complementOf;
import static java.util.EnumSet.of;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * A {@link FileLocator} which delegate to another using a strategy to choose it.
 */
public class StrategyFileLocator
        implements FileLocator {
    
    /**
     * {@inheritDoc}
     * 
     * @see com.mycila.testing.plugins.jetty.locator.FileLocator#locate(java.lang.String)
     */
    public Iterable<File> locate(
            final String path)
        throws FileNotFoundException
    {
        final Strategy strategy = findStrategy(path);
        final Iterable<File> files = strategy.getLocator().locate(strategy.cleanPath(path));
        
        return files;
    }
    

    /**
     * Enumeration of available file locator strategy.
     */
    public enum Strategy {
        
        /**
         * Default path is either relative or absolute.
         */
        DEFAULT("", new PathFileLocator()),
        
        /**
         * Enable the following to be java regular expression for this path.
         */
        REG("reg:", new RegFileLocator()),
        
        /**
         * Enable the following to be ant path expression for this path.
         */
        ANT("ant:", new AntFileLocator()),
        
        /**
         * Enable the the following to be system property expression for this path.
         */
        SYS("sys:", new SysFileLocator());
        
        Strategy(
                final String code,
                final FileLocator fileLocator)
        {
            this.code = code;
            this.fileLocator = fileLocator;
        }
        

        boolean matches(
                final String path)
        {
            final boolean matches = (path != null)
                    && ((!DEFAULT.equals(this) && path.startsWith(this.code)) || DEFAULT.equals(this));
            return matches;
        }
        

        String cleanPath(
                final String path)
        {
            if (path == null) {
                throw new NullPointerException("path should not be null");
            }
            if (!this.matches(path)) {
                throw new IllegalArgumentException("path should starts with " + this.code);
            }
            
            final int from = this.code.length();
            final int to = path.length();
            
            final String clean = path.substring(from, to);
            
            return clean;
        }
        

        FileLocator getLocator()
        {
            return this.fileLocator;
        }
        

        static Strategy findStrategy(
                final String path)
        {
            for (final Strategy strategy : complementOf(of(DEFAULT))) {
                final boolean matches = strategy.matches(path);
                if (matches) {
                    return strategy;
                }
            }
            
            return Strategy.DEFAULT;
        }
        

        private final String code;
        
        private final FileLocator fileLocator;
        
    }
    
}
