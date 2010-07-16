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
import com.mycila.plugin.annotation.From;
import com.mycila.plugin.spi.internal.AnnotationUtils;
import com.mycila.plugin.spi.invoke.InvokableMember;
import com.mycila.plugin.spi.invoke.Invokables;

import java.lang.annotation.Annotation;
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
    private final List<PluginImport<?>> imports;

    private InjectionPoint(InvokableMember<?> invokable, List<PluginImport<?>> imports) {
        this.invokable = invokable;
        this.imports = Collections.unmodifiableList(new ArrayList<PluginImport<?>>(imports));
    }

    public List<PluginImport<?>> getImports() {
        return imports;
    }

    @Override
    public String toString() {
        return "Injection Point " + invokable.getMember();
    }

    static InjectionPoint from(Method method, Object plugin) {
        List<Binding<?>> parameters = Binding.fromParameters(method);
        Annotation[][] allAnnotations = method.getParameterAnnotations();
        List<PluginImport<?>> imports = new ArrayList<PluginImport<?>>(parameters.size());
        for (int i = 0; i < allAnnotations.length; i++)
            imports.add(PluginImport.from(findFrom(allAnnotations[i]), parameters.get(i)));
        return new InjectionPoint(Invokables.get(method, plugin), imports);
    }

    static InjectionPoint from(Field field, Object plugin) {
        InvokableMember<?> invokable = Invokables.get(field, plugin);
        List<PluginImport<?>> imports = new ArrayList<PluginImport<?>>(1);
        imports.add(PluginImport.from(findFrom(field.getAnnotations()), Binding.fromInvokable(invokable)));
        return new InjectionPoint(invokable, imports);
    }

    private static Class<?> findFrom(Annotation... annotations) {
        From from = AnnotationUtils.findAnnotation(From.class, annotations);
        return from == null ? null : from.value();
    }
}
