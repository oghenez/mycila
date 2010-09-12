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

import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.mycila.inject.util.Members;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class MethodHandlerTypeListener<A extends Annotation> implements TypeListener {
    private final Class<A> annotationType;
    private final Class<? extends MethodHandler<A>> handlerClass;

    public MethodHandlerTypeListener(Class<A> annotationType, Class<? extends MethodHandler<A>> handlerClass) {
        this.annotationType = annotationType;
        this.handlerClass = handlerClass;
    }

    @Override
    public <I> void hear(final TypeLiteral<I> type, TypeEncounter<I> encounter) {
        final Provider<? extends MethodHandler<A>> provider = encounter.getProvider(handlerClass);
        encounter.register(new InjectionListener<I>() {
            @Override
            public void afterInjection(I injectee) {
                MethodHandler<A> handler = provider.get();
                for (Method method : Members.findAnnotatedMethods(type.getRawType(), annotationType))
                    handler.handle(type, injectee, method);
            }
        });
    }
}
