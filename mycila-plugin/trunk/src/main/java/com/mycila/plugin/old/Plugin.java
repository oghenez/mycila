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

import java.util.List;

/**
 * Defines a plugin and its dependencies / order of execution
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Plugin {

    /**
     * Get the list of plugins that should be executed before this one
     *
     * @return empty list, or a list of plugin names
     */
    List<String> getBefore();

    /**
     * Get the list of plugins that should be executed after this one
     *
     * @return empty list, or a list of plugin names
     */
    List<String> getAfter();
}
