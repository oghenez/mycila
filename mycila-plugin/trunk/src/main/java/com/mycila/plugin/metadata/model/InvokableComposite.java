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

package com.mycila.plugin.metadata.model;

import com.mycila.plugin.Invokable;
import com.mycila.plugin.metadata.InvokeException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class InvokableComposite implements Invokable {

    private final List<Invokable> invokables = new LinkedList<Invokable>();

    public InvokableComposite(Invokable... invokables) {
        add(invokables);
    }

    public InvokableComposite(Iterable<Invokable> invokables) {
        add(invokables);
    }

    public void add(Invokable... invokables) {
        this.invokables.addAll(Arrays.asList(invokables));
    }

    public void add(Iterable<Invokable> invokables) {
        for (Invokable invokable : invokables)
            this.invokables.add(invokable);
    }

    @Override
    public Object invoke(Object... args) throws InvokeException {
        Object res = null;
        for (Invokable invokable : invokables)
            res = invokable.invoke(args);
        return res;
    }
}
