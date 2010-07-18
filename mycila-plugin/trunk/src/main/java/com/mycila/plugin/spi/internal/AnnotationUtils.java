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
import java.lang.reflect.Proxy;

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

}