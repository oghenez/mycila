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

package com.mycila.plugin;

import com.mycila.plugin.annotation.BindingAnnotation;
import com.mycila.plugin.spi.internal.AnnotationUtils;
import com.mycila.plugin.spi.internal.Assert;
import com.mycila.plugin.spi.internal.StringUtils;
import com.mycila.plugin.spi.invoke.InvokableMember;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Binding<T> {

    private final TypeLiteral<T> type;
    private final List<Annotation> annotations;

    private Binding(TypeLiteral<T> type) {
        this(type, Collections.<Annotation>emptyList());
    }

    private Binding(TypeLiteral<T> type, Collection<Annotation> annotations) {
        this.type = type;
        this.annotations = Collections.unmodifiableList(new ArrayList<Annotation>(annotations));
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public TypeLiteral<T> getType() {
        return type;
    }

    @Override
    public String toString() {
        return annotations.isEmpty() ? type.toString() :
                StringUtils.arrayToCommaDelimitedString(annotations.toArray(new Object[annotations.size()])) + " " + type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Binding binding = (Binding) o;
        return annotations.equals(binding.annotations) && type.equals(binding.type);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + annotations.hashCode();
        return result;
    }

    public static <T> Binding<T> fromInvokable(InvokableMember<T> invokable) {
        Collection<Annotation> annotations = new LinkedHashSet<Annotation>();
        for (Annotation annotation : invokable.getMember().getAnnotations())
            if (annotation.annotationType().isAnnotationPresent(BindingAnnotation.class))
                annotations.add(annotation);
        return new Binding<T>(invokable.getType(), annotations);
    }

    public static List<Binding<?>> fromParameters(Method method) {
        List<Binding<?>> bindings = new ArrayList<Binding<?>>(5);
        List<TypeLiteral<?>> types = TypeLiteral.get(method.getDeclaringClass()).getParameterTypes(method);
        Annotation[][] annots = method.getParameterAnnotations();
        for (int i = 0; i < types.size(); i++) {
            // annotations
            Collection<Annotation> annotations = new LinkedHashSet<Annotation>();
            for (Annotation annotation : annots[i])
                if (annotation.annotationType().isAnnotationPresent(BindingAnnotation.class))
                    annotations.add(annotation);
            // type
            TypeLiteral<?> type = types.get(i);
            if (type.getRawType() == Provider.class) {
                if(type.getType() instanceof Class)
                    type = TypeLiteral.get(Object.class);
                else {
                    Type[] args = ((ParameterizedType) type.getType()).getActualTypeArguments();
                    if (args.length == 0)
                        throw new PluginException("Missing type argument for provider at ");
                    try {
                        type = TypeLiteral.get(args[0]);
                    } catch (Exception e) {
                        throw new PluginException("Illegal binding found for parameter " + type + " on method " + method + " : " + e.getMessage());
                    }
                }
            }
            bindings.add(binding(type, annotations));
        }
        return Collections.unmodifiableList(bindings);
    }

    public static <T> Binding<T> get(Class<T> type) {
        return new Binding<T>(TypeLiteral.get(type));
    }

    public static <T> Binding<T> get(Class<T> type, Annotation... annotations) {
        return get(TypeLiteral.get(type), annotations);
    }

    public static <T> Binding<T> get(Class<T> type, Class<? extends Annotation>... annotations) {
        return get(TypeLiteral.get(type), annotations);
    }

    public static <T> Binding<T> get(TypeLiteral<T> type) {
        return new Binding<T>(type);
    }

    public static <T> Binding<T> get(TypeLiteral<T> type, Annotation... annotations) {
        for (Annotation annotation : annotations)
            Assert.state(annotation.annotationType().isAnnotationPresent(BindingAnnotation.class));
        return new Binding<T>(type, Arrays.asList(annotations));
    }

    public static <T> Binding<T> get(TypeLiteral<T> type, Class<? extends Annotation>... annotations) {
        Annotation[] annots = new Annotation[annotations.length];
        for (int i = 0; i < annotations.length; i++) {
            Assert.state(annotations[i].isAnnotationPresent(BindingAnnotation.class));
            annots[i] = AnnotationUtils.buildRandomAnnotation(annotations[i]);
        }
        return get(type, annots);
    }

    private static <T> Binding<T> binding(TypeLiteral<T> type, Collection<Annotation> annotations) {
        return new Binding<T>(type, annotations);
    }
}


