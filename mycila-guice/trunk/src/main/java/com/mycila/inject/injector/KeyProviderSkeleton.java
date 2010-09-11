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

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class KeyProviderSkeleton<A extends Annotation> implements KeyProvider<A> {
    @Override
    public Key<?> getKey(AnnotatedMember<?, A> annotatedMember) {
        return Key.get(annotatedMember.getType());
    }

    @Override
    public List<Key<?>> getParameterKeys(AnnotatedMember<?, A> annotatedMember) {
        List<TypeLiteral<?>> types = annotatedMember.getType().getParameterTypes(annotatedMember.getMember());
        List<Key<?>> keys = new ArrayList<Key<?>>();
        for (TypeLiteral<?> type : types)
            keys.add(Key.get(type));
        return keys;
    }
}