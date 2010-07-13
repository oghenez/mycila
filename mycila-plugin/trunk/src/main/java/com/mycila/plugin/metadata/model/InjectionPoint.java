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

package com.mycila.plugin.metadata.model;

import com.mycila.plugin.invoke.InvokableMember;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class InjectionPoint {

    private final PluginMetadata metadata;
    private final InvokableMember<?> invokable; // TODO
    private final List<PluginImport> imports;

    InjectionPoint(PluginMetadata metadata, InvokableMember<?> invokable, List<PluginImport> imports) {
        this.metadata = metadata;
        this.invokable = invokable;
        this.imports = Collections.unmodifiableList(new ArrayList<PluginImport>(imports));
    }

    public PluginMetadata getPluginMetadata() {
        return metadata;
    }

    public List<PluginImport> getImports() {
        return imports;
    }

    @Override
    public String toString() {
        return "InjectionPoint " + invokable.getMember();
    }
}
