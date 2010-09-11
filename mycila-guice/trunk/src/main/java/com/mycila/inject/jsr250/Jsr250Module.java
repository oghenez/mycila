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
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.BindingScopingVisitor;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.mycila.inject.BinderHelper;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
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
        BinderHelper.in(binder).bindAnnotationInjector(Resource.class, Jsr250KeyProvider.class);
        binder.bindListener(Matchers.any(), new TypeListener() {
            @Override
            public <I> void hear(final TypeLiteral<I> injectableType, final TypeEncounter<I> encounter) {
                encounter.register(new InjectionListener<I>() {
                    @Override
                    public void afterInjection(I injectee) {
                        Jsr250.postConstruct(injectee);
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
