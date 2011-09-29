/**
 * Copyright (C) 2010 Mycila <mathieu.carbou@gmail.com>
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

package com.mycila.inject.service;

import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.mycila.inject.annotation.OverrideModule;

import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class ServiceModules {

    private ServiceModules() {
    }

    public static Module loadFromClasspath() {
        return loadFromClasspath(Module.class);
    }

    public static Module loadFromClasspath(Class<? extends Module> moduleType) {
        List<Module> runtime = new LinkedList<Module>();
        List<Module> overrides = new LinkedList<Module>();
        for (Module module : ServiceLoader.load(moduleType)) {
            if (module.getClass().isAnnotationPresent(OverrideModule.class))
                overrides.add(module);
            else
                runtime.add(module);
        }
        return overrides.isEmpty() ? Modules.combine(runtime) : Modules.override(runtime).with(overrides);
    }

}
