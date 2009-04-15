/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
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

package com.mycila.testing.plugin.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.binder.ScopedBindingBuilder;
import com.mycila.testing.core.api.TestContext;
import static com.mycila.testing.core.introspect.Filters.*;
import com.mycila.testing.core.plugin.DefaultTestPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Guice1TestPlugin extends DefaultTestPlugin {

    @Override
    public void prepareTestInstance(final TestContext context) {
        context.setAttribute("guice.providers", new ArrayList<Provider<?>>());
        GuiceContext ctx = context.introspector().testClass().getAnnotation(GuiceContext.class);

        // create modules
        List<Module> modules = new ArrayList<Module>();
        modules.add(new AbstractModule() {
            @Override
            protected void configure() {
                bind(ProviderSetup.class).toInstance(new ProviderSetup() {
                    @Inject
                    void inject(Injector injector) {
                        for (Provider<?> provider : context.<List<Provider<?>>>removeAttribute("guice.providers")) {
                            injector.injectMembers(provider);
                        }
                    }
                });
            }
        });
        modules.addAll(contextualModules(ctx));
        modules.addAll(providedModules(context));
        modules.add(bindings(context));
        modules.add(providedBindings(context));
        if (context.introspector().instance() instanceof Module) {
            modules.add((Module) context.introspector().instance());
        }

        // create injector
        Injector injector = Guice.createInjector(findStage(ctx), modules);
        context.setAttribute("com.google.inject.Injector", injector);

        injector.injectMembers(context.introspector().instance());
    }

    private Module bindings(final TestContext context) {
        return new Module() {
            public void configure(Binder binder) {
                for (final Field field : context.introspector().selectFields(fieldsAnnotatedBy(Bind.class))) {
                    Guice1TestPlugin.this.configure(context, binder, field.getGenericType(), field.getAnnotation(Bind.class), new InjectedProvider<Object>() {
                        public Object getInternal() {
                            return context.introspector().get(field);
                        }
                    });

                }
            }
        };
    }

    private Module providedBindings(final TestContext context) {
        return new Module() {
            public void configure(Binder binder) {
                for (final Method method : context.introspector().selectMethods(methodsAnnotatedBy(Bind.class))) {
                    Guice1TestPlugin.this.configure(context, binder, method.getGenericReturnType(), method.getAnnotation(Bind.class), new InjectedProvider<Object>() {
                        public Object getInternal() {
                            return context.introspector().invoke(method);
                        }
                    });

                }
            }
        };
    }

    @SuppressWarnings({"unchecked"})
    private <T> void configure(TestContext context, Binder binder, Type type, Bind annotation, InjectedProvider<T> provider) {
        AnnotatedBindingBuilder<T> builder1 = (AnnotatedBindingBuilder<T>) binder.bind(TypeLiteral.get(type));
        LinkedBindingBuilder<T> builder2 = annotation.annotatedBy().equals(NoAnnotation.class) ? builder1 : builder1.annotatedWith(annotation.annotatedBy());
        ScopedBindingBuilder builder3 = builder2.toProvider(provider);
        if (!annotation.scope().equals(NoAnnotation.class)) {
            builder3.in(annotation.scope());
        }
        context.<List<InjectedProvider<T>>>attribute("guice.providers").add(provider);
    }

    @SuppressWarnings({"unchecked"})
    private List<Module> providedModules(TestContext ctx) {
        List<Module> modules = new ArrayList<Module>();
        for (Method method : ctx.introspector().selectMethods(and(methodsOfType(Module.class), methodsAnnotatedBy(ModuleProvider.class)))) {
            modules.add((Module) ctx.introspector().invoke(method));
        }
        for (Method method : ctx.introspector().selectMethods(and(methodsOfType(Module[].class), methodsAnnotatedBy(ModuleProvider.class)))) {
            modules.addAll(Arrays.asList((Module[]) ctx.introspector().invoke(method)));
        }
        for (Method method : ctx.introspector().selectMethods(and(methodsOfType(Iterable.class), methodsAnnotatedBy(ModuleProvider.class)))) {
            for (Module module : (Iterable<Module>) ctx.introspector().invoke(method)) {
                modules.add(module);
            }
        }
        return modules;
    }

    private Stage findStage(GuiceContext ctx) {
        return ctx == null ? Stage.DEVELOPMENT : ctx.stage();
    }

    private List<Module> contextualModules(GuiceContext context) {
        List<Module> modules = new ArrayList<Module>();
        if (context != null) {
            for (Class<? extends Module> moduleClass : context.value()) {
                try {
                    modules.add(moduleClass.newInstance());
                } catch (Exception e) {
                    throw new IllegalStateException(String.format("Error instanciating module class '%s': %s", moduleClass.getName(), e.getMessage()), e);
                }
            }
        }
        return modules;
    }

    private static interface ProviderSetup {
    }

    private static abstract class InjectedProvider<T> implements Provider<T> {

        @Inject
        Injector injector;

        public final T get() {
            T t = getInternal();
            injector.injectMembers(t);
            return t;
        }

        protected abstract T getInternal();
    }

}
