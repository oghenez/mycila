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

package com.mycila.plugin.spi.model;

import com.mycila.plugin.PluginException;
import com.mycila.plugin.annotation.BindingAnnotation;
import com.mycila.plugin.spi.invoke.InvokableMember;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
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
    private final Collection<Annotation> annotations;
    private final int hashCode;
    private final boolean provided;

    private Binding(TypeLiteral<T> type, Collection<Annotation> annotations) {
        this.provided = Types.isProvided(type);
        this.type = MoreTypes.canonicalizeForKey(provided ? Types.<T>withoutProvider(type) : type);
        this.annotations = Collections.unmodifiableCollection(new LinkedHashSet<Annotation>(annotations));
        int result = 31 * type.hashCode();
        for (Annotation annotation : annotations)
            result += annotation.hashCode();
        this.hashCode = result;
    }

    public Collection<Annotation> getAnnotations() {
        return annotations;
    }

    public TypeLiteral<T> getType() {
        return type;
    }

    public boolean isProvided() {
        return provided;
    }

    @Override
    public String toString() {
        return "Binding of " + (annotations.isEmpty() ? type.toString() : type + " with annotations " + annotations);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Binding binding = (Binding) o;
        return type.equals(binding.type)
                && annotations.size() == binding.annotations.size()
                && annotations.containsAll(binding.annotations)
                && binding.annotations.containsAll(annotations);
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    static <T> Binding<T> fromInvokable(InvokableMember<T> invokable) {
        Collection<Annotation> annotations = new LinkedHashSet<Annotation>();
        for (Annotation annotation : invokable.getMember().getAnnotations())
            if (annotation.annotationType().isAnnotationPresent(BindingAnnotation.class))
                annotations.add(annotation);
        try {
            return binding(invokable.getType(), annotations);
        } catch (Exception e) {
            throw new PluginException("Unable to resolve binding type " + invokable.getType() + " for " + invokable + " : " + e.getMessage(), e);
        }
    }

    static List<Binding<?>> fromParameters(Method method) {
        List<Binding<?>> bindings = new ArrayList<Binding<?>>(5);
        List<TypeLiteral<?>> types = TypeLiteral.get(method.getDeclaringClass()).getParameterTypes(method);
        Annotation[][] annots = method.getParameterAnnotations();
        for (int i = 0; i < types.size(); i++) {
            // annotations
            Collection<Annotation> annotations = new LinkedHashSet<Annotation>();
            for (Annotation annotation : annots[i])
                if (annotation.annotationType().isAnnotationPresent(BindingAnnotation.class))
                    annotations.add(annotation);
            try {
                bindings.add(binding(types.get(i), annotations));
            } catch (Exception e) {
                throw new PluginException("Unable to resolve binding type " + types.get(i) + " for " + method + " : " + e.getMessage(), e);
            }
        }
        return Collections.unmodifiableList(bindings);
    }

    public static <T> Binding<T> get(Class<T> type) {
        return get(TypeLiteral.get(type));
    }

    public static <T> Binding<T> get(Class<T> type, Annotation... annotations) {
        return get(TypeLiteral.get(type), annotations);
    }

    public static <T> Binding<T> get(Class<T> type, Class<? extends Annotation>... annotations) {
        return get(TypeLiteral.get(type), annotations);
    }

    public static <T> Binding<T> get(TypeLiteral<T> type) {
        return binding(type, Collections.<Annotation>emptyList());
    }

    public static <T> Binding<T> get(TypeLiteral<T> type, Annotation... annotations) {
        for (Annotation annotation : annotations)
            checkBindingAnnotations(annotation.annotationType());
        return binding(type, Arrays.asList(annotations));
    }

    public static <T> Binding<T> get(TypeLiteral<T> type, Class<? extends Annotation>... annotations) {
        for (Class<? extends Annotation> annotation : annotations)
            checkBindingAnnotations(annotation);
        Annotation[] annots = new Annotation[annotations.length];
        for (int i = 0; i < annotations.length; i++) {
            annots[i] = AnnotationMetadata.buildRandomAnnotation(annotations[i]);
        }
        return get(type, annots);
    }

    private static <T> Binding<T> binding(TypeLiteral<T> type, Collection<Annotation> annotations) {
        return new Binding<T>(type, annotations);
    }

    private static void checkBindingAnnotations(Class<? extends Annotation> annotation) {
        if (!annotation.isAnnotationPresent(BindingAnnotation.class))
            throw new IllegalArgumentException("Annotation @" + annotation.getName() + " is not a binding annotation. Binding annotations are annotated with @BindingAnnotation");
    }

}

