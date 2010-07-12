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

import net.sf.cglib.reflect.FastMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class InjectionPoint {

    private final PluginMetadata metadata;
    private final FastMethod method; //TODO
    private final List<PluginImport> dependencies;

    private InjectionPoint(PluginMetadata metadata, FastMethod method, List<PluginImport> dependencies) {
        this.metadata = metadata;
        this.method = method;
        this.dependencies = Collections.unmodifiableList(new ArrayList<PluginImport>(dependencies));
    }

    public PluginMetadata getMetadata() {
        return metadata;
    }

    public List<PluginImport> getDependencies() {
        return dependencies;
    }

    public static InjectionPoint create(PluginMetadata metadata, FastMethod method, List<PluginImport> dependencies) {
        return new InjectionPoint(metadata, method, dependencies);
    }
}
