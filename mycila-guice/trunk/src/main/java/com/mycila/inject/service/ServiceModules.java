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
