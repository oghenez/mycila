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

package old;

import com.google.inject.Binder;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;
import com.mycila.util.ServiceClassLoader;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ServiceLoaderModule {

    public static <T> Module of(Class<T> serviceClass, Annotation annotation) {
        return of(TypeLiteral.get(serviceClass), annotation);
    }

    public static <T> Module of(TypeLiteral<T> serviceType, Annotation annotation) {
        return of(serviceType, Key.get(setOf(serviceType), annotation));
    }

    public static <T> Module of(Class<T> serviceClass, Class<? extends Annotation> annotationType) {
        return of(TypeLiteral.get(serviceClass), annotationType);
    }

    public static <T> Module of(TypeLiteral<T> serviceType, Class<? extends Annotation> annotationType) {
        return of(serviceType, Key.get(setOf(serviceType), annotationType));
    }

    public static <T> Module of(Class<T> serviceClass) {
        return of(TypeLiteral.get(serviceClass));
    }

    public static <T> Module of(TypeLiteral<T> serviceType) {
        return of(serviceType, Key.get(setOf(serviceType)));
    }

    private static <T> Module of(TypeLiteral<T> serviceType, Key<Set<T>> key) {
        return new ServiceLoaderModuleImpl<T>(serviceType, key, null);
    }

    public static ServiceLoaderModuleWithCL withClassLoader(Class<? extends ClassLoader> classLoaderType) {
        return withClassLoader(Key.get(classLoaderType));
    }

    public static ServiceLoaderModuleWithCL withClassLoader(Class<? extends ClassLoader> classLoaderType, Class<? extends Annotation> annot) {
        return withClassLoader(Key.get(classLoaderType, annot));
    }

    public static ServiceLoaderModuleWithCL withClassLoader(Class<? extends ClassLoader> classLoaderType, Annotation annot) {
        return withClassLoader(Key.get(classLoaderType, annot));
    }

    public static ServiceLoaderModuleWithCL withClassLoader(final Key<? extends ClassLoader> classLoaderKey) {
        return new ServiceLoaderModuleWithCL() {
            @Override
            public <T> Module of(Class<T> serviceClass) {
                return of(TypeLiteral.get(serviceClass));
            }

            @Override
            public <T> Module of(TypeLiteral<T> serviceType) {
                return of(serviceType, Key.get(setOf(serviceType)));
            }

            @Override
            public <T> Module of(Class<T> serviceClass, Annotation annotation) {
                return of(TypeLiteral.get(serviceClass), annotation);
            }

            @Override
            public <T> Module of(TypeLiteral<T> serviceType, Annotation annotation) {
                return of(serviceType, Key.get(setOf(serviceType), annotation));
            }

            @Override
            public <T> Module of(Class<T> serviceClass, Class<? extends Annotation> annotationType) {
                return of(TypeLiteral.get(serviceClass), annotationType);
            }

            @Override
            public <T> Module of(TypeLiteral<T> serviceType, Class<? extends Annotation> annotationType) {
                return of(serviceType, Key.get(setOf(serviceType), annotationType));
            }

            private <T> Module of(TypeLiteral<T> serviceType, Key<Set<T>> key) {
                return new ServiceLoaderModuleImpl<T>(serviceType, key, classLoaderKey);
            }

        };
    }

    @SuppressWarnings("unchecked")
    private static <T> TypeLiteral<Set<T>> setOf(TypeLiteral<T> elementType) {
        Type type = Types.setOf(elementType.getType());
        return (TypeLiteral<Set<T>>) TypeLiteral.get(type);
    }

    public static interface ServiceLoaderModuleWithCL {
        <T> Module of(Class<T> serviceClass);

        <T> Module of(TypeLiteral<T> serviceType);

        <T> Module of(Class<T> serviceClass, Annotation annotation);

        <T> Module of(TypeLiteral<T> serviceType, Annotation annotation);

        <T> Module of(Class<T> serviceClass, Class<? extends Annotation> annotationType);

        <T> Module of(TypeLiteral<T> serviceType, Class<? extends Annotation> annotationType);
    }

    private static final class ServiceLoaderModuleImpl<T> implements Module {

        private final TypeLiteral<T> serviceType;
        private final Key<Set<T>> setKey;
        private final Key<? extends ClassLoader> classLoaderKey;

        private ServiceLoaderModuleImpl(TypeLiteral<T> serviceType, Key<Set<T>> setKey, Key<? extends ClassLoader> classLoaderKey) {
            this.serviceType = serviceType;
            this.setKey = setKey;
            this.classLoaderKey = classLoaderKey;
        }

        @Override
        public void configure(Binder binder) {
            binder.bind(setKey).toProvider(new Provider<Set<T>>() {

                @Inject
                Injector injector;

                @Override
                public Set<T> get() {
                    Set<T> instances = new HashSet<T>();
                    ServiceClassLoader<T> loader = classLoaderKey == null ?
                            ServiceClassLoader.<T>load(serviceType.getRawType(), Thread.currentThread().getContextClassLoader()) :
                            ServiceClassLoader.<T>load(serviceType.getRawType(), injector.getInstance(classLoaderKey));
                    for (Class<T> clazz : loader)
                        instances.add(injector.getInstance(clazz));
                    return instances;
                }
            });
        }
    }

}
