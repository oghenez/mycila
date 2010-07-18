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

package com.mycila.plugin.spi.internal;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.LinkedHashMap;
import java.util.Map;

public final class AnnotationUtils {

    private AnnotationUtils() {
    }

    public static <T extends Annotation> T findAnnotation(Class<T> annotationClass, Annotation... annotations) {
        for (Annotation annotation : annotations)
            if (annotationClass.isInstance(annotation))
                return annotationClass.cast(annotation);
        return null;
    }

    @SuppressWarnings({"unchecked"})
    public static <T extends Annotation> T buildRandomAnnotation(Class<T> annotationClass) {
        return (T) Proxy.newProxyInstance(
                annotationClass.getClassLoader(),
                new Class<?>[]{annotationClass},
                new AnnotationHandler(AnnotationMetadata.randomAnnotation(annotationClass)));
    }

    public static Object getRandomDefault(Class<?> type) {
        Object o = ObjectUtils.defaultValue(type);
        if (o != null) return o;
        // this is a class, enum, String or array
        if (type.isArray()) return Array.newInstance(type, 0);
        if (type == String.class) return "";
        if (type == Class.class) return Void.class;
        try {
            if (type.isEnum()) return Enum.valueOf((Class<? extends Enum>) type, type.getDeclaredFields()[0].getName());
        } catch (Exception e) {
            throw new UnsupportedOperationException("Unable to randomize annotation: cannot get an enum for " + type);
        }
        throw new UnsupportedOperationException("Type: " + type.getName());
    }

    public static Map<String, Object> getDefaults(Class<? extends Annotation> annotationClass) {
        Map<String, Object> defaults = new LinkedHashMap<String, Object>();
        try {
            Field field = Class.class.getDeclaredField("annotationType");
            field.setAccessible(true);
            sun.reflect.annotation.AnnotationType type = (sun.reflect.annotation.AnnotationType) field.get(annotationClass);
            defaults.putAll(type.memberDefaults());
        } catch (Exception ignored) {
        }
        return defaults;
    }
}