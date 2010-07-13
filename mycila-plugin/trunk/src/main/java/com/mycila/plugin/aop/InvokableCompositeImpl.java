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

package com.mycila.plugin.aop;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class InvokableCompositeImpl<T> implements InvokableComposite<T> {

    private final List<Invokable<T>> invokables = new LinkedList<Invokable<T>>();

    InvokableCompositeImpl() {
    }

    @Override
    public void add(Invokable<T> invokable) {
        this.invokables.add(invokable);
    }

    @Override
    public void addAll(Iterable<Invokable<T>> invokables) {
        for (Invokable<T> invokable : invokables)
            this.invokables.add(invokable);
    }

    @Override
    public Iterator<Invokable<T>> iterator() {
        return invokables.iterator();
    }

    @Override
    public T invoke(Object... args) throws InvokeException {
        T res = null;
        for (Invokable<? extends T> invokable : invokables)
            res = invokable.invoke(args);
        return res;
    }

}
