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

package com.mycila.guice.spi;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Collector {

    private static final ThreadLocal<List<String>> collector = new ThreadLocal<List<String>>() {
        @Override
        protected List<String> initialValue() {
            return new LinkedList<String>();
        }
    };

    public static void clear() {
        collector.get().clear();
    }

    public static void add(String s) {
        collector.get().add(s);
    }

    public static String[] get() {
        return collector.get().toArray(new String[collector.get().size()]);
    }
}
