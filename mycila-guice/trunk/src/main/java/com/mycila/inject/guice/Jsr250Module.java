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

package com.mycila.inject.guice;

import com.google.inject.Binder;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.ProvisionException;
import com.google.inject.Scope;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.BindingScopingVisitor;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.mycila.inject.Closer;
import com.mycila.inject.annotation.SingletonMarker;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Jsr250Module implements Module {

    private final Set<Scope> scopes = new HashSet<Scope>();
    private final Set<Class<? extends Annotation>> scopeAnnotations = new HashSet<Class<? extends Annotation>>();

    @Override
    public void configure(Binder binder) {
        binder.bindListener(Matchers.any(), new TypeListener() {
            @Override
            public <I> void hear(TypeLiteral<I> iTypeLiteral, TypeEncounter<I> encounter) {
                encounter.register(new InjectionListener<I>() {
                    @Override
                    public void afterInjection(I injectee) {
                        invoke(injectee, javax.annotation.PostConstruct.class);
                    }
                });
            }
        });
        Closer closer = new Closer() {
            @Inject
            Injector injector;

            @Override
            public void close() {
                for (final Binding<?> binding : injector.getAllBindings().values()) {
                    Boolean res = binding.acceptScopingVisitor(new BindingScopingVisitor<Boolean>() {
                        @Override
                        public Boolean visitEagerSingleton() {
                            return true;
                        }

                        @Override
                        public Boolean visitScope(Scope scope) {
                            return scope.equals(Scopes.SINGLETON)
                                    || scope.getClass().isAnnotationPresent(SingletonMarker.class)
                                    || scopes.contains(scope)
                                    || scope instanceof MappedScope && ((MappedScope) scope).isSingleton();
                        }

                        @Override
                        public Boolean visitScopeAnnotation(Class<? extends Annotation> scopeAnnotation) {
                            return scopeAnnotation.equals(Singleton.class)
                                    || scopeAnnotation.isAnnotationPresent(SingletonMarker.class)
                                    || scopeAnnotations.contains(scopeAnnotation);
                        }

                        @Override
                        public Boolean visitNoScoping() {
                            return false;
                        }
                    });
                    if (res != null && res)
                        invoke(binding.getProvider().get(), javax.annotation.PreDestroy.class);
                    for (Scope scope : injector.getScopeBindings().values()) {
                        invoke(scope, javax.annotation.PreDestroy.class);
                    }
                }
            }
        };
        binder.bind(Closer.class).toInstance(closer);
        binder.requestInjection(closer);
    }

    public Jsr250Module addCloseableScopes(Scope... scopes) {
        this.scopes.addAll(Arrays.asList(scopes));
        return this;
    }

    public Jsr250Module addCloseableScopes(Class<? extends Annotation>... scopeAnnotations) {
        this.scopeAnnotations.addAll(Arrays.asList(scopeAnnotations));
        return this;
    }

    private static void invoke(Object injectee, Class<? extends Annotation> annotationClass) {
        Class<?> c = injectee.getClass();
        if (c.getName().contains("$$"))
            c = c.getSuperclass();
        List<Method> methods = Methods.listAll(c, Methods.METHOD_WITHOUT_PARAMETER.and(Matchers.annotatedWith(annotationClass)));
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
}
