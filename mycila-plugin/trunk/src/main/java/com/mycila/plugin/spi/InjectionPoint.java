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

package com.mycila.plugin.spi;

import com.mycila.plugin.Binding;
import com.mycila.plugin.spi.invoke.InvokableMember;
import com.mycila.plugin.spi.invoke.Invokables;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class InjectionPoint {

    private final InvokableMember<?> invokable; // TODO
    private final List<Binding<?>> bindings;

    private InjectionPoint(InvokableMember<?> invokable, List<? extends Binding<?>> bindings) {
        this.invokable = invokable;
        this.bindings = Collections.unmodifiableList(new ArrayList<Binding<?>>(bindings));
    }

    public List<Binding<?>> getBindings() {
        return bindings;
    }

    @Override
    public String toString() {
        return "InjectionPoint " + invokable.getMember().getName() + " at " + invokable.getMember().getDeclaringClass().getName();
    }

    static InjectionPoint from(Method method, Object plugin) {
        return new InjectionPoint(Invokables.get(method, plugin), Binding.fromParameters(method));
    }

    static InjectionPoint from(Field field, Object plugin) {
        InvokableMember<?> invokable = Invokables.get(field, plugin);
        return new InjectionPoint(invokable, asList(Binding.fromInvokable(invokable)));
    }

    private static List<? extends Binding<?>> asList(Binding<?> b) {
        return Collections.singletonList(b);
    }

}
