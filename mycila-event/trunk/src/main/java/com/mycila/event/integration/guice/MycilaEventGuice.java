/**
 * Copyright (C) 2009 Mathieu Carbou <mathieu.carbou@gmail.com>
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

package com.mycila.event.integration.guice;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.binder.ScopedBindingBuilder;
import com.mycila.event.api.annotation.AnnotationProcessor;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class MycilaEventGuice {
    private MycilaEventGuice() {
    }

    public static <T> Provider<T> publisher(final Class<T> clazz) {
        return new Provider<T>() {
            @Inject
            Provider<AnnotationProcessor> annotationProcessor;

            @Override
            public T get() {
                return annotationProcessor.get().proxy(clazz);
            }
        };
    }

    public static <T> ScopedBindingBuilder bindPublisher(Binder binder, Class<T> clazz) {
        return binder.bind(clazz).toProvider(publisher(clazz));
    }
}
