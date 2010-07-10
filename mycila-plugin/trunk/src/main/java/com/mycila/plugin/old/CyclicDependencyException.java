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

package com.mycila.plugin.old;

import com.mycila.plugin.PluginException;

import static java.lang.String.*;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class CyclicDependencyException extends PluginException {
    private static final long serialVersionUID = -6644476561325060279L;

    private final SortedMap<String, Plugin> cyclics;

    public CyclicDependencyException(SortedMap<String, Plugin> cyclics) {
        super(format("Cyclic dependencies have been found amongst these plugins:\n%s", info(cyclics)));
        this.cyclics = Collections.unmodifiableSortedMap(cyclics);
    }

    public SortedMap<String, Plugin> getCyclics() {
        return cyclics;
    }

    private static String info(SortedMap<String, Plugin> cyclics) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Plugin> entry : cyclics.entrySet()) {
            sb.append("- Plugin '").append(entry.getKey()).append("'").append("\n");
            sb.append("    - befores: ").append(entry.getValue().getBefore()).append("\n");
            sb.append("    - afters: ").append(entry.getValue().getAfter()).append("\n");
        }
        return sb.toString();
    }

}
