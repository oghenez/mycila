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

package test;

import com.mycila.plugin.classpath.DefaultClassLoader;
import com.mycila.plugin.classpath.Loader;
import com.mycila.plugin.discovery.AnnotatedPluginDiscovery;
import com.mycila.plugin.discovery.PluginDiscovery;
import com.mycila.plugin.metadata.AnnotationMetadataBuilder;
import com.mycila.plugin.metadata.MetadataBuilder;
import com.mycila.plugin.metadata.model.PluginMetadata;
import com.mycila.plugin.util.ClassUtils;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Main {
    public static void main(String[] args) throws Exception {
        Loader loader = new DefaultClassLoader(ClassUtils.getDefaultClassLoader());
        PluginDiscovery pluginDiscovery = new AnnotatedPluginDiscovery(loader);
        MetadataBuilder metadataBuilder = new AnnotationMetadataBuilder();
        for (Class<?> pluginClass : pluginDiscovery.scan()) {
            Object plugin = pluginClass.getConstructor().newInstance();
            PluginMetadata metadata = metadataBuilder.getMetadata(plugin);
            System.out.println(metadata);
        }
    }
}
