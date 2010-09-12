/**
 * Copyright (C) 2010 Mycila <mathieu.carbou@gmail.com>
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
import com.mycila.inject.BinderHelper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class KeyProviderSkeleton<A extends Annotation> implements KeyProvider<A> {

    @Override
    public Key<?> getKey(TypeLiteral<?> injectedType, Field injectedMember, A resourceAnnotation) {
        for (Annotation annotation : injectedMember.getAnnotations())
            if (BinderHelper.isBindingAnnotation(annotation.annotationType()))
                return Key.get(injectedType.getFieldType(injectedMember), annotation);
        return Key.get(injectedType.getFieldType(injectedMember));
    }

    @Override
    public List<Key<?>> getParameterKeys(TypeLiteral<?> injectedType, Method injectedMember, A resourceAnnotation) {
        Annotation[][] parameterAnnotations = injectedMember.getParameterAnnotations();
        List<TypeLiteral<?>> parameterTypes = injectedType.getParameterTypes(injectedMember);
        List<Key<?>> keys = new ArrayList<Key<?>>();
        for (int i = 0; i < parameterTypes.size(); i++)
            keys.add(buildKey(parameterTypes.get(i), parameterAnnotations[i]));
        return keys;
    }

    private Key<?> buildKey(TypeLiteral<?> type, Annotation[] annotations) {
        for (Annotation annotation : annotations)
            if (BinderHelper.isBindingAnnotation(annotation.annotationType()))
                return Key.get(type, annotation);
        return Key.get(type);
    }
}
