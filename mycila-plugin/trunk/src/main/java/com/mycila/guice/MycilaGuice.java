/**
 * Copyright (C) 2010 Mathieu Carbou <mathieu.carbou@gmail.com>
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

package com.mycila.guice;

import com.google.inject.Module;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class MycilaGuice {

    private Loader loader = new com.mycila.guice.spi.DefaultLoader();
    private PluginDiscovery discovery = new com.mycila.guice.spi.CustomPluginDiscovery();
    private final LinkedList<Module> modules = new LinkedList<Module>();

    public MycilaGuice withLoader(Loader loader) {
        this.loader = loader;
        return this;
    }

    public MycilaGuice withDiscovery(PluginDiscovery discovery) {
        this.discovery = discovery;
        return this;
    }

    public MycilaGuice withAnnotationDiscovery() {
        return withDiscovery(new PluginDiscovery() {
            @Override
            public Iterable<? extends Class<?>> scan() throws PluginDiscoveryException {
                return new com.mycila.guice.spi.AnnotatedPluginDiscovery(loader).scan();
            }
        });
    }

    public MycilaGuice withMetaInfServicesDiscovery() {
        return withDiscovery(new PluginDiscovery() {
            @Override
            public Iterable<? extends Class<?>> scan() throws PluginDiscoveryException {
                return new com.mycila.guice.spi.JdkServicePluginDiscovery(loader).scan();
            }
        });
    }

    public MycilaGuice addModule(Module module) {
        modules.add(module);
        return this;
    }

    public MycilaGuice addModules(Module... modules) {
        this.modules.addAll(Arrays.asList(modules));
        return this;
    }

    public MycilaGuice addModules(Iterable<? extends Module> modules) {
        for (Module module : modules)
            this.modules.add(module);
        return this;
    }

    public PluginManager build() {
        return com.mycila.guice.spi.ConcurrentPluginManager.build(discovery, modules);
    }

}