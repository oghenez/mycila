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

package com.mycila.guice;

import com.google.inject.Binder;
import com.google.inject.ProvisionException;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.mycila.guice.annotation.PostInject;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class LifeCycle {
    private LifeCycle() {
    }

    public static void install(Binder binder) {
        TypeListener listener = postInjector(PostInject.class);
        binder.requestInjection(listener);
        binder.bindListener(Matchers.any(), listener);
    }

    public static TypeListener postInjector(Class<? extends Annotation> annotationClass) {
        return new PostInjectListener(annotationClass);
    }


    private static final class PostInjectListener implements TypeListener {
        private final Class<? extends Annotation> annotationClass;

        private PostInjectListener(Class<? extends Annotation> annotationClass) {
            this.annotationClass = annotationClass;
        }

        @Override
        public <I> void hear(final TypeLiteral<I> type, TypeEncounter<I> encounter) {
            encounter.register(new InjectionListener<I>() {
                @Override
                public void afterInjection(I injectee) {
                    List<Method> methods = Methods.listAll(
                            type.getRawType(),
                            Methods.METHOD_WITHOUT_PARAMETER.and(Matchers.annotatedWith(annotationClass)));
                    for (Method method : methods) {
                        if (!method.isAccessible())
                            method.setAccessible(true);
                        try {
                            method.invoke(injectee);
                        } catch (IllegalAccessException e) {
                            throw new ProvisionException(e.getMessage(), e);
                        } catch (InvocationTargetException e) {
                            throw new ProvisionException(e.getTargetException().getMessage(), e.getTargetException());
                        }
                    }
                }
            });
        }
    }
}
