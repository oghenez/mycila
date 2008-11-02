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

package old;

import com.mycila.testing.plugin.guice.Bind;
import com.mycila.testing.plugin.guice.GuiceContext;
import com.mycila.testing.plugin.guice.ModuleProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Stage;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class GuicePlugin {

    private final Object testInstance;

    public GuicePlugin(Object testInstance) {
        this.testInstance = testInstance;
    }

    @SuppressWarnings({"unchecked"})
    public List<Module> buildModules() {
        List<Module> modules = new ArrayList<Module>();
        final Class<?> testClass = testInstance.getClass();

        // if the test class declares some static module to instanciate
        GuiceContext guiceTest = testClass.getAnnotation(GuiceContext.class);
        if (guiceTest != null) {
            for (Class<? extends Module> moduleClass : guiceTest.modules()) {
                try {
                    modules.add(moduleClass.newInstance());
                } catch (Exception e) {
                    throw new RuntimeException(String.format("Cannot load module '%s': %s", moduleClass.getName(), e.getMessage()), e);
                }
            }
        }

        // if the test class declares some methods annotated by ModuleProvider
        for (Method method : testClass.getMethods()) {
            if (method.getAnnotation(ModuleProvider.class) != null) {
                Object obj = null;
                try {
                    obj = method.invoke(testInstance);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(String.format("Cannot invoke public method '%s.%s()': %s", testClass.getName(), method.getName(), e.getMessage()), e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(String.format("Error invoking public method '%s.%s()': %s", testClass.getName(), method.getName(), e.getTargetException().getMessage()), e.getTargetException());
                }
                if (obj instanceof Module) {
                    modules.add((Module) obj);
                } else if (obj instanceof Iterable) {
                    for (Module module : ((Iterable<? extends Module>) obj)) {
                        modules.add(module);
                    }
                } else if (obj instanceof Module[]) {
                    modules.addAll(Arrays.asList(((Module[]) obj)));
                } else {
                    throw new IllegalStateException(String.format("Illegal method signature. Method %s.%s must return one of Module, Module[] or Iterable<Module>", testClass.getName(), method.getName()));
                }
            }
        }

        // if the class has some @Bind annotations on some fields
        modules.add(new AbstractModule() {
            @SuppressWarnings({"unchecked"})
            protected void configure() {
                try {
                    for (Field field : ClassUtils.getFields(testClass, Bind.class)) {
                        bind((Class<Object>) field.getType()).toInstance(field.get(testInstance));
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        });

        // if the test class implements Module
        if (testInstance instanceof Module) {
            modules.add((Module) testInstance);
        }

        return modules;
    }

    public Stage stage() {
        GuiceContext guiceTest = testInstance.getClass().getAnnotation(GuiceContext.class);
        return guiceTest == null ? Stage.DEVELOPMENT : guiceTest.stage();
    }
}
