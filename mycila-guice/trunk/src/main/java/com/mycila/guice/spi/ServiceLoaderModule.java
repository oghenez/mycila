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

package com.mycila.guice.spi;

import com.google.inject.*;
import com.google.inject.util.Types;
import com.mycila.guice.Loader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ServiceLoaderModule {

    public static <T> Module of(Class<T> serviceClass, Annotation annotation) {
        return of(TypeLiteral.get(serviceClass), annotation);
    }

    public static <T> Module of(TypeLiteral<T> serviceType, Annotation annotation) {
        return of(serviceType, Key.get(listOf(serviceType), annotation));
    }

    public static <T> Module of(Class<T> serviceClass, Class<? extends Annotation> annotationType) {
        return of(TypeLiteral.get(serviceClass), annotationType);
    }

    public static <T> Module of(TypeLiteral<T> serviceType, Class<? extends Annotation> annotationType) {
        return of(serviceType, Key.get(listOf(serviceType), annotationType));
    }

    public static <T> Module of(Class<T> serviceClass) {
        return of(TypeLiteral.get(serviceClass));
    }

    public static <T> Module of(TypeLiteral<T> serviceType) {
        return of(serviceType, Key.get(listOf(serviceType)));
    }

    public static <T> Module of(TypeLiteral<T> serviceType, Key<List<T>> key) {
        return new ServiceLoaderModuleImpl<T>(serviceType, key, null);
    }

    public static WithLoader withLoader(Class<? extends Loader> loaderType) {
        return withClassLoader(Key.get(loaderType));
    }

    public static WithLoader withLoader(Class<? extends Loader> loaderType, Class<? extends Annotation> annot) {
        return withClassLoader(Key.get(loaderType, annot));
    }

    public static WithLoader withLoader(Class<? extends Loader> loaderType, Annotation annot) {
        return withClassLoader(Key.get(loaderType, annot));
    }

    public static WithLoader withClassLoader(final Key<? extends Loader> loaderType) {
        return new WithLoader() {
            @Override
            public <T> Module of(Class<T> serviceClass) {
                return of(TypeLiteral.get(serviceClass));
            }

            @Override
            public <T> Module of(TypeLiteral<T> serviceType) {
                return of(serviceType, Key.get(listOf(serviceType)));
            }

            @Override
            public <T> Module of(Class<T> serviceClass, Annotation annotation) {
                return of(TypeLiteral.get(serviceClass), annotation);
            }

            @Override
            public <T> Module of(TypeLiteral<T> serviceType, Annotation annotation) {
                return of(serviceType, Key.get(listOf(serviceType), annotation));
            }

            @Override
            public <T> Module of(Class<T> serviceClass, Class<? extends Annotation> annotationType) {
                return of(TypeLiteral.get(serviceClass), annotationType);
            }

            @Override
            public <T> Module of(TypeLiteral<T> serviceType, Class<? extends Annotation> annotationType) {
                return of(serviceType, Key.get(listOf(serviceType), annotationType));
            }

            public <T> Module of(TypeLiteral<T> serviceType, Key<List<T>> key) {
                return new ServiceLoaderModuleImpl<T>(serviceType, key, loaderType);
            }

        };
    }

    @SuppressWarnings("unchecked")
    private static <T> TypeLiteral<List<T>> listOf(TypeLiteral<T> elementType) {
        Type type = Types.listOf(elementType.getType());
        return (TypeLiteral<List<T>>) TypeLiteral.get(type);
    }

    public static interface WithLoader {
        <T> Module of(Class<T> serviceClass);

        <T> Module of(TypeLiteral<T> serviceType);

        <T> Module of(Class<T> serviceClass, Annotation annotation);

        <T> Module of(TypeLiteral<T> serviceType, Annotation annotation);

        <T> Module of(Class<T> serviceClass, Class<? extends Annotation> annotationType);

        <T> Module of(TypeLiteral<T> serviceType, Class<? extends Annotation> annotationType);
    }

    private static final class ServiceLoaderModuleImpl<T> implements Module {

        private final TypeLiteral<T> serviceType;
        private final Key<List<T>> listKey;
        private final Key<? extends Loader> loaderKey;

        private ServiceLoaderModuleImpl(TypeLiteral<T> serviceType, Key<List<T>> listKey, Key<? extends Loader> loaderKey) {
            this.serviceType = serviceType;
            this.listKey = listKey;
            this.loaderKey = loaderKey;
        }

        @Override
        public void configure(Binder binder) {
            binder.bind(listKey).toProvider(new Provider<List<T>>() {

                @Inject
                Injector injector;

                @Override
                public List<T> get() {
                    List<T> instances = new LinkedList<T>();
                    ServiceClassLoader<T> loader = loaderKey == null ?
                            ServiceClassLoader.<T>load(getType(), new DefaultLoader()) :
                            ServiceClassLoader.<T>load(getType(), injector.getInstance(loaderKey));
                    for (Class<T> clazz : loader)
                        instances.add(injector.getInstance(clazz));
                    return instances;
                }
            });
        }

        @SuppressWarnings({"unchecked"})
        private Class<T> getType() {
            return (Class<T>) serviceType.getRawType();
        }
    }

}
