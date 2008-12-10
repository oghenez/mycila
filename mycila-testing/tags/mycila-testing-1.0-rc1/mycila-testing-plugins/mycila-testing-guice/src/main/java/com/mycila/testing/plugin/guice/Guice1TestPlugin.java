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

import com.google.inject.*;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.binder.ScopedBindingBuilder;
import com.mycila.testing.core.AbstractTestPlugin;
import com.mycila.testing.core.Context;
import com.mycila.testing.core.TestPluginException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Guice1TestPlugin extends AbstractTestPlugin {

    public void prepareTestInstance(Context context) {
        GuiceContext ctx = context.getTest().getTargetClass().getAnnotation(GuiceContext.class);
        List<Module> modules = new ArrayList<Module>();
        modules.addAll(contextualModules(ctx));
        modules.addAll(providedModules(context));
        modules.add(bindings(context));
        modules.add(providedBindings(context));
        if (context.getTest().getTarget() instanceof Module) {
            modules.add((Module) context.getTest().getTarget());
        }
        Injector injector = Guice.createInjector(findStage(ctx), modules);
        context.setAttribute("com.google.inject.Injector", injector);
        injector.injectMembers(context.getTest().getTarget());
    }

    private Module bindings(final Context context) {
        return new Module() {
            public void configure(Binder binder) {
                for (final Field field : context.getTest().getFieldsAnnotatedWith(Bind.class)) {
                    Guice1TestPlugin.this.configure(binder, field.getGenericType(), field.getAnnotation(Bind.class), new Provider<Object>() {
                        public Object get() {
                            return context.getTest().get(field);
                        }
                    });

                }
            }
        };
    }

    private Module providedBindings(final Context context) {
        return new Module() {
            public void configure(Binder binder) {
                for (final Method method : context.getTest().getMethodsAnnotatedWith(Bind.class)) {
                    Guice1TestPlugin.this.configure(binder, method.getGenericReturnType(), method.getAnnotation(Bind.class), new Provider<Object>() {
                        public Object get() {
                            return context.getTest().invoke(method);
                        }
                    });

                }
            }
        };
    }

    @SuppressWarnings({"unchecked"})
    private <T> void configure(Binder binder, Type type, Bind annotation, Provider<T> provider) {
        AnnotatedBindingBuilder<T> builder1 = (AnnotatedBindingBuilder<T>) binder.bind(TypeLiteral.get(type));
        LinkedBindingBuilder<T> builder2 = annotation.annotatedBy().equals(NoAnnotation.class) ? builder1 : builder1.annotatedWith(annotation.annotatedBy());
        ScopedBindingBuilder builder3 = builder2.toProvider(provider);
        if (!annotation.scope().equals(NoAnnotation.class)) {
            builder3.in(annotation.scope());
        }
    }

    private List<Module> providedModules(Context ctx) {
        List<Module> modules = new ArrayList<Module>();
        for (Method method : ctx.getTest().getMethodsOfTypeAnnotatedWith(Module.class, ModuleProvider.class)) {
            modules.add((Module) ctx.getTest().invoke(method));
        }
        for (Method method : ctx.getTest().getMethodsOfTypeAnnotatedWith(Module[].class, ModuleProvider.class)) {
            modules.addAll(Arrays.asList((Module[]) ctx.getTest().invoke(method)));
        }
        for (Method method : ctx.getTest().getMethodsOfTypeAnnotatedWith(Iterable.class, ModuleProvider.class)) {
            //noinspection unchecked
            for (Module module : (Iterable<Module>) ctx.getTest().invoke(method)) {
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
                    throw new TestPluginException(e, "Error instanciating module class '%s': %s", moduleClass.getName(), e.getMessage());
                }
            }
        }
        return modules;
    }
}
