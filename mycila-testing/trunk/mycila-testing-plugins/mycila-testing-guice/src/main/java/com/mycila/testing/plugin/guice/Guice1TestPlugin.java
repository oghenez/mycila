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

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.mycila.testing.core.AbstractTestPlugin;
import com.mycila.testing.core.Context;
import com.mycila.testing.core.TestPluginException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Guice1TestPlugin extends AbstractTestPlugin {

    public void prepareTestInstance(Context context) {
        GuiceContext ctx = context.getTest().getTargetClass().getAnnotation(GuiceContext.class);

        // build module list
        List<Module> modules = new ArrayList<Module>();
        modules.addAll(contextualModules(ctx));
        modules.addAll(providedModules(context));
        if (context.getTest().getTarget() instanceof Module) {
            modules.add((Module) context.getTest().getTarget());
        }

        // build injector
        Injector injector = Guice.createInjector(findStage(ctx), modules);
        context.setAttribute("com.google.inject.Injector", injector);
        injector.injectMembers(context.getTest().getTarget());
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
