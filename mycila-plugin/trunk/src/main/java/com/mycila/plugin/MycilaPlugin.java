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

package com.mycila.plugin;

import com.mycila.plugin.err.PluginDiscoveryException;
import com.mycila.plugin.spi.AnnotatedPluginDiscovery;
import com.mycila.plugin.spi.ConcurrentPluginManager;
import com.mycila.plugin.spi.CustomPluginDiscovery;
import com.mycila.plugin.spi.DefaultLoader;
import com.mycila.plugin.spi.JdkServicePluginDiscovery;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class MycilaPlugin {

    private Loader loader = new DefaultLoader();
    private PluginDiscovery discovery = new CustomPluginDiscovery();

    public MycilaPlugin withLoader(Loader loader) {
        this.loader = loader;
        return this;
    }

    public MycilaPlugin withDiscovery(PluginDiscovery discovery) {
        this.discovery = discovery;
        return this;
    }

    public MycilaPlugin withAnnotationDiscovery() {
        return withDiscovery(new PluginDiscovery() {
            @Override
            public Iterable<? extends Class<?>> scan() throws PluginDiscoveryException {
                return new AnnotatedPluginDiscovery(loader).scan();
            }
        });
    }

    public MycilaPlugin withMetaInfServicesDiscovery() {
        return withDiscovery(new PluginDiscovery() {
            @Override
            public Iterable<? extends Class<?>> scan() throws PluginDiscoveryException {
                return new JdkServicePluginDiscovery(loader).scan();
            }
        });
    }

    public PluginManager build() {
        return new ConcurrentPluginManager(discovery);
    }

}
