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

import com.google.inject.*;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.BindingScopingVisitor;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.mycila.inject.annotation.ConcurrentSingleton;

import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class LifeCycle {
    private LifeCycle() {
    }

    public static void install(Binder binder) {
        TypeListener listener = createPostInjector(javax.annotation.PostConstruct.class);
        Closer closer = createCloser(javax.annotation.PreDestroy.class);
        closer.register(Singleton.class);
        closer.register(ConcurrentSingleton.class);
        binder.bindListener(Matchers.any(), listener);
        binder.bind(Closer.class).toInstance(closer);
        binder.requestInjection(listener);
        binder.requestInjection(closer);
    }

    public static TypeListener createPostInjector(Class<? extends Annotation> annotationClass) {
        return new PostInjectListener(annotationClass);
    }

    public static Closer createCloser(Class<? extends Annotation> annotationClass) {
        return new CloserImpl(annotationClass);
    }

    public static Closer getCloser(Injector injector) {
        return injector.getInstance(Closer.class);
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
                    invoke(injectee, annotationClass);
                }
            });
        }
    }

    private static final class CloserImpl implements Closer {
        private final Set<Scope> scopesToClose = new HashSet<Scope>();
        private final Set<Class<? extends Annotation>> scopeAnnotationToClose = new HashSet<Class<? extends Annotation>>();

        private final Class<? extends Annotation> annotationClass;
        @Inject
        Injector injector;

        private CloserImpl(Class<? extends Annotation> annotationClass) {
            this.annotationClass = annotationClass;
        }

        @Override
        public void register(Class<? extends Annotation> annotationClass) {
            scopeAnnotationToClose.add(annotationClass);
        }

        @Override
        public void register(Scope scope) {
            scopesToClose.add(scope);
            if (scope instanceof ScopeWithAnnotation)
                scopeAnnotationToClose.add(((ScopeWithAnnotation) scope).getScopeAnnotation());
        }

        @Override
        public synchronized void close() {
            final Set<Scope> scopesToCloseLocal = new HashSet<Scope>(this.scopesToClose);
            final Set<Class<? extends Annotation>> scopeAnnotationToCloseLocal = new HashSet<Class<? extends Annotation>>(this.scopeAnnotationToClose);
            for (Map.Entry<Class<? extends Annotation>, Scope> entry : injector.getScopeBindings().entrySet()) {
                if (scopeAnnotationToCloseLocal.contains(entry.getKey()))
                    scopesToCloseLocal.add(entry.getValue());
                if (scopesToCloseLocal.contains(entry.getValue()))
                    scopeAnnotationToCloseLocal.add(entry.getKey());
            }
            for (Binding<?> binding : injector.getAllBindings().values()) {
                Boolean res = binding.acceptScopingVisitor(new BindingScopingVisitor<Boolean>() {
                    @Override
                    public Boolean visitEagerSingleton() {
                        return true;
                    }

                    @Override
                    public Boolean visitScope(Scope scope) {
                        return scopesToCloseLocal.contains(scope) ? true : null;
                    }

                    @Override
                    public Boolean visitScopeAnnotation(Class<? extends Annotation> scopeAnnotation) {
                        return scopeAnnotationToCloseLocal.contains(scopeAnnotation) ? true : null;
                    }

                    @Override
                    public Boolean visitNoScoping() {
                        return false;
                    }
                });
                if (res != null && res)
                    invoke(binding.getProvider().get(), annotationClass);
                for (Scope scope : injector.getScopeBindings().values()) {
                    invoke(scope, annotationClass);
                }
            }
        }
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
