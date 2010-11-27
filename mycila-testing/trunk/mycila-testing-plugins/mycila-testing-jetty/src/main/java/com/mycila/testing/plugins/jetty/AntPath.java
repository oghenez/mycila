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

import static com.google.common.base.Predicates.in;
import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.transform;
import static com.google.common.collect.Lists.newArrayList;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

/**
 * <ul>
 * <li>? matches one character
 * <li>* matches zero or more characters
 * <li>** matches zero or more 'directories' in a path
 * </ul>
 */
class AntPath {

    public AntPath(
            final String antPath)
    {
        this.antPath = antPath;
    }


    public boolean matches(
            final String path)
    {
        final boolean matches = path.matches(this.toRegex());

        return matches;
    }


    String toRegex()
    {
        final Iterable<String> splits = this.split();

        final String joined = Joiner.on("").skipNulls().join(transform(splits, new Function<String, String>() {
            public String apply(
                    final String input)
            {
                final String regex;

                if (input.length() == 0) {
                    regex = null;
                }
                else if ("?".equals(input)) {
                    regex = "[^/]";
                }
                else if ("*".equals(input)) {
                    regex = "[^/]*";
                }
                else if ("**".equals(input)) {
                    regex = ".*?";
                }
                else if ("**/".equals(input)) {
                    regex = "([^/]+?/)*?";
                }
                else {
                    regex = "\\Q" + input + "\\E";
                }

                return regex;
            }
        }));

        return joined;
    }


    private Iterable<String> split()
    {
        // pat?/**/t*t/** => pat,?,/,**/,t,*,t/,**
        // pat,[^/],/,([^/]+?/)*?,t,[^/]*,t/,([^/]+?/)*?

        Iterable<String> splits = newArrayList(this.antPath);
        splits = concat(transform(splits, new MultiSplitFunction("?", "**/", "**", "*")));

        return splits;
    }

    private final String antPath;

    static class MultiSplitFunction
            implements Function<String, Iterable<String>> {

        public MultiSplitFunction(
                final Iterable<String> separators)
        {
            this.separators = separators;
        }


        public MultiSplitFunction(
                final String... separators)
        {
            this(newArrayList(separators));
        }


        public Iterable<String> apply(
                final String input)
        {
            final Collection<String> toIgnores = newArrayList();

            Iterable<String> splits = newArrayList(input);
            for (final String separator : this.separators) {
                splits = this.split(splits, separator, toIgnores);
                toIgnores.add(separator);
            }

            return splits;
        }


        private Iterable<String> split(
                final Iterable<String> inputs,
                final String separator,
                final Iterable<String> toIgnore)
        {
            final Function<String, Iterable<String>> splitter = new BooleanSwitchFunction<String, Iterable<String>>(
                    not(in(newArrayList(toIgnore))),
                    new SplitFunction(separator),
                    new InputAsIterableFunction());
            final Iterable<String> splits = concat(transform(inputs, splitter));

            return splits;
        }

        private final Iterable<String> separators;

    }

    static class SplitFunction
            implements Function<String, Iterable<String>> {

        public SplitFunction(
                final String separator)
        {
            this.separator = separator;
        }


        public Iterable<String> apply(
                final String input)
        {
            final Iterable<String> parts = Splitter.on(this.separator).split(input);
            final Iterable<String> splits = insertBetweenEach(parts, this.separator);

            return splits;
        }


        private static Iterable<String> insertBetweenEach(
                final Iterable<String> elements,
                final String separator)
        {
            final List<String> list = newArrayList(elements);

            for (final ListIterator<String> iter = list.listIterator(); iter.hasNext();) {
                if ((iter.nextIndex() > 0) && iter.hasNext()) {
                    iter.add(separator);
                }
                iter.next();
            }

            return list;
        }

        private final String separator;

    }

    static class InputAsIterableFunction
            implements Function<String, Iterable<String>> {

        public Iterable<String> apply(
                final String input)
        {
            return newArrayList(input);
        }

    }

}
