/**
 * Copyright (C) 2010 mycila.com <mathieu.carbou@gmail.com>
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

package com.mycila.inject.injector;

import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.Annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class KeyProviderSkeleton<A extends Annotation> implements KeyProvider<A> {
    @Override
    public <M extends Member & AnnotatedElement> Key<?> getKey(AnnotatedMember<M, A> member) {
        for (Annotation annotation : member.getMember().getAnnotations())
            if (Annotations.isBindingAnnotation(annotation.annotationType()))
                return Key.get(member.getMemberType(), annotation);
        return Key.get(member.getMemberType());
    }

    @Override
    public <M extends Member & AnnotatedElement> List<Key<?>> getParameterKeys(AnnotatedMember<M, A> member) {
        Annotation[][] parameterAnnotations;
        if (member.getMember() instanceof Method)
            parameterAnnotations = ((Method) member.getMember()).getParameterAnnotations();
        else if (member.getMember() instanceof Constructor)
            parameterAnnotations = ((Constructor) member.getMember()).getParameterAnnotations();
        else
            throw new IllegalStateException("Unsupported member: " + member.getMember());
        List<TypeLiteral<?>> types = member.getBoundType().getParameterTypes(member.getMember());
        List<Key<?>> keys = new ArrayList<Key<?>>();
        for (int i = 0; i < types.size(); i++)
            keys.add(buildKey(types.get(i), parameterAnnotations[i]));
        return keys;
    }

    private Key<?> buildKey(TypeLiteral<?> type, Annotation[] annotations) {
        for (Annotation annotation : annotations)
            if (Annotations.isBindingAnnotation(annotation.annotationType()))
                return Key.get(type, annotation);
        return Key.get(type);
    }
}
