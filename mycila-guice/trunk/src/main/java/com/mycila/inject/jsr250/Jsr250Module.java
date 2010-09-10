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

package com.mycila.inject.jsr250;

import com.google.inject.Binder;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Scope;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.util.Maps;
import com.google.inject.internal.util.Sets;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.BindingScopingVisitor;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Jsr250Module implements Module {

    private final Set<Scope> scopes = new HashSet<Scope>();
    private final Set<Class<? extends Annotation>> scopeAnnotations = new HashSet<Class<? extends Annotation>>();
    private final BindingScopingVisitor<Boolean> visitor = Jsr250.destroyableVisitor(scopes, scopeAnnotations);

    @Override
    public void configure(Binder binder) {
        binder.bind(Jsr250Injector.class).to(Jsr250InjectorImpl.class).in(Singleton.class);
        binder.bindListener(Matchers.any(), new TypeListener() {
            @Override
            public <I> void hear(final TypeLiteral<I> injectableType, final TypeEncounter<I> encounter) {
                encounter.register(new InjectionListener<I>() {
                    @Override
                    public void afterInjection(I injectee) {
                        Set<Field> processedFields = Sets.newLinkedHashSet();
                        Map<MethodKey, Method> processedMethods = Maps.newLinkedHashMap();
                        Class<?> type = injectableType.getRawType();
                        while (type != Object.class && type != null) {
                            Field[] fields = type.getDeclaredFields();
                            for (Field field : fields) {
                                if (processedFields.add(field) && field.isAnnotationPresent(Resource.class)) {
                                    encounter.register(new InjectionListener<I>() {
                                        @Override
                                        public void afterInjection(I injectee) {
                                            
                                        }
                                    });
                                }
                            }

                            Method[] methods = type.getDeclaredMethods();
                            for (final Method method : methods) {
                                MethodKey key = new MethodKey(method);
                                if (processedMethods.get(key) == null) {
                                    processedMethods.put(key, method);
                                    bindAnnotationInjectionToMember(encounter, startType, method);
                                }
                            }

                            Class<?> supertype = type.getSuperclass();
                            if (supertype == Object.class) {
                                break;
                            }
                            startType = startType.getSupertype(supertype);
                        }

                        //Post construction
                        Jsr250.invoke(injectee, javax.annotation.PostConstruct.class);
                    }
                });
            }
        });
        Jsr250Destroyer closer = new Jsr250Destroyer() {
            @Inject
            Injector injector;

            @Override
            public void preDestroy() {
                for (Jsr250Element element : Jsr250.destroyables(injector, visitor)) {
                    element.preDestroy();
                }
            }
        };
        binder.bind(Jsr250Destroyer.class).toInstance(closer);
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

}
