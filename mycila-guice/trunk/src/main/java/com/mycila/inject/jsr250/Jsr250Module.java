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
import com.google.inject.Binding;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Scope;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.BindingScopingVisitor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.mycila.inject.BinderHelper.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Jsr250Module implements Module {

    private final Set<Scope> scopes = new HashSet<Scope>();
    private final Set<Class<? extends Annotation>> scopeAnnotations = new HashSet<Class<? extends Annotation>>();

    @Override
    public void configure(Binder binder) {
        binder.bind(Jsr250Injector.class).to(Jsr250InjectorImpl.class).in(Singleton.class);
        in(binder)
                .bindAnnotationInjector(Resource.class, Jsr250KeyProvider.class)
                .bindAfterInjection(PostConstruct.class, Jsr250PostConstructHandler.class)
                .bind(Jsr250Destroyer.class, new Jsr250Destroyer() {
                    @Inject
                    Injector injector;

                    @Override
                    public void preDestroy() {
                        BindingScopingVisitor<Boolean> visitor = new BindingScopingVisitor<Boolean>() {
                            @Override
                            public Boolean visitEagerSingleton() {
                                return true;
                            }

                            @Override
                            public Boolean visitScope(Scope scope) {
                                return scope.getClass().isAnnotationPresent(Jsr250Singleton.class)
                                        || scopes.contains(scope);
                            }

                            @Override
                            public Boolean visitScopeAnnotation(Class<? extends Annotation> scopeAnnotation) {
                                return scopeAnnotation.isAnnotationPresent(Jsr250Singleton.class)
                                        || scopeAnnotations.contains(scopeAnnotation);
                            }

                            @Override
                            public Boolean visitNoScoping() {
                                return false;
                            }
                        };
                        for (Binding<?> binding : injector.getAllBindings().values())
                            if (Scopes.isSingleton(binding) || binding.acceptScopingVisitor(visitor))
                                Jsr250.preDestroy(binding.getKey().getTypeLiteral(), binding.getProvider().get());
                        for (Scope scope : injector.getScopeBindings().values())
                            Jsr250.preDestroy(TypeLiteral.get(scope.getClass()), scope);
                    }
                });
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
