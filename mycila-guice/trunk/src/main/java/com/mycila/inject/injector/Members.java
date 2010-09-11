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

import com.google.inject.TypeLiteral;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Members {

    private Members() {
    }

    public static <A extends Annotation> List<AnnotatedMember<Field, A>> getAnnotatedFields(TypeLiteral<?> type, final Class<A> annotationType) {
        List<AnnotatedMember<Field, A>> fields = new LinkedList<AnnotatedMember<Field, A>>();
        Class<?> raw = type.getRawType();
        while (raw != null && raw != Object.class) {
            for (final Field field : raw.getDeclaredFields()) {
                if (field.isAnnotationPresent(annotationType)) {
                    final TypeLiteral<?> f = type.getFieldType(field);
                    fields.add(new AnnotatedMember<Field, A>() {
                        @Override
                        public TypeLiteral<?> getType() {
                            return f;
                        }

                        @Override
                        public Field getMember() {
                            return field;
                        }

                        @Override
                        public A getAnnotation() {
                            return field.getAnnotation(annotationType);
                        }
                    });
                }
            }
            raw = raw.getSuperclass();
            type = raw == null ? null : type.getSupertype(raw);
        }
        return fields;
    }

}