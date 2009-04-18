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
package com.mycila.testing.core.introspect;

import static com.mycila.testing.core.api.Ensure.*;

import java.util.LinkedList;
import java.util.List;

/**
 * This class is used to select fields or methods more easily.
 * You can implement you own filters or use existing ones from the
 * {@link Filters} class and compose them
 * with and, or, not, ...
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class Filter<T> {

    protected final List<T> elements = new LinkedList<T>();

    public List<T> select() {
        return elements;
    }

    protected boolean accept(T object) {
        return true;
    }

    public void add(T element) {
        notNull("Element", element);
        if (accept(element)) {
            elements.add(element);
        }
    }

}
