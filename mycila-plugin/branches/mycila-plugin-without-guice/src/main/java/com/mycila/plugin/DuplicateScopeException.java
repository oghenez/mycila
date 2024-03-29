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

import java.lang.reflect.AnnotatedElement;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DuplicateScopeException extends PluginException {
    private static final long serialVersionUID = 1;

    public DuplicateScopeException(AnnotatedElement element) {
        super("Too many scopes found at " + element);
    }
}
