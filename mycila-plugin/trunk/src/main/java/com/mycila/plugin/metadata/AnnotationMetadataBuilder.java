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

package com.mycila.plugin.metadata;

import com.mycila.plugin.annotation.ActivateAfter;
import com.mycila.plugin.annotation.ActivateBefore;
import com.mycila.plugin.annotation.Export;
import com.mycila.plugin.annotation.From;
import com.mycila.plugin.annotation.Import;
import com.mycila.plugin.annotation.OnStart;
import com.mycila.plugin.annotation.OnStop;
import com.mycila.plugin.annotation.Param;
import com.mycila.plugin.annotation.Plugin;
import com.mycila.plugin.annotation.Scope;
import com.mycila.plugin.metadata.model.PluginImport;
import com.mycila.plugin.metadata.model.PluginMetadata;
import com.mycila.plugin.scope.defaults.None;
import com.mycila.plugin.util.AopUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class AnnotationMetadataBuilder implements MetadataBuilder {

    @Override
    public final PluginMetadata getMetadata(Object plugin) {
        Class<?> pluginClass = AopUtils.getTargetClass(plugin.getClass());

        Plugin pluginAnnot = pluginClass.getAnnotation(Plugin.class);

        PluginMetadata.Builder builder = PluginMetadata.create(pluginClass, plugin)
                .withName(pluginAnnot == null || pluginAnnot.name().length() == 0 ? pluginClass.getSimpleName() : pluginAnnot.name())
                .withDescription(pluginAnnot == null || pluginAnnot.description().length() == 0 ? pluginClass.getName() : pluginAnnot.description())
                .withActivateBefore(getActivateBefore(pluginClass))
                .withActivateAfter(getActivateAfter(pluginClass));

        for (Method method : pluginClass.getMethods()) {

            if (method.isAnnotationPresent(OnStart.class))
                builder.addOnStart(method.getName());

            if (method.isAnnotationPresent(OnStop.class))
                builder.addOnStop(method.getName());

            if (method.isAnnotationPresent(Export.class)) {
                Scope scope = method.getAnnotation(Scope.class);
                Map<String, String> parameters = new HashMap<String, String>();
                if (scope != null)
                    for (Param param : scope.params())
                        parameters.put(param.name(), param.value());
                builder.addExport(
                        method.getName(),
                        scope == null ? None.class : scope.value(),
                        parameters);
            }

            if (method.isAnnotationPresent(Import.class)) {
                Class<?>[] params = method.getParameterTypes();
                Annotation[][] annots = method.getParameterAnnotations();
                List<PluginImport> dependencies = new ArrayList<PluginImport>(params.length);
                for (int i = 0; i < params.length; i++) {
                    Class<?> from = PluginImport.FROM_ANY_PLUGIN;
                    for (Annotation annot : annots[i])
                        if (From.class.isInstance(annot)) {
                            from = ((From) annot).value();
                            break;
                        }
                    dependencies.add(PluginImport.create(params[i], from));
                }
                builder.addInjectionPoint(method.getName(), dependencies);
            }
        }

        return builder.build();
    }

    private Set<Class<?>> getActivateBefore(Class<?> pluginClass) {
        Set<Class<?>> plugins = new HashSet<Class<?>>(2);
        ActivateBefore activateBefore = pluginClass.getAnnotation(ActivateBefore.class);
        if (activateBefore != null && activateBefore.value().length > 0)
            plugins.addAll(Arrays.asList(activateBefore.value()));
        return plugins;
    }

    private Set<Class<?>> getActivateAfter(Class<?> pluginClass) {
        Set<Class<?>> plugins = new HashSet<Class<?>>(2);
        ActivateAfter activateAfter = pluginClass.getAnnotation(ActivateAfter.class);
        if (activateAfter != null && activateAfter.value().length > 0)
            plugins.addAll(Arrays.asList(activateAfter.value()));
        return plugins;
    }

}
