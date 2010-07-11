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

package com.mycila.plugin.scope;

import com.mycila.plugin.Invokable;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface ScopeContext<T> {

    /**
     * Get the invokable returning the instance of T 
     *
     * @return The service
     */
    Invokable getInvokable();

    /**
     * Check if a scope parameter exists
     *
     * @param name Scope parameter name
     * @return true if exist
     */
    boolean hasParameter(String name);

    /**
     * Get a scope parameter
     *
     * @param name Scope parameter name
     * @return the parameter value
     * @throws MissingScopeParameterException If no parameter exist with this name
     */
    String getParameter(String name) throws MissingScopeParameterException;

    String toString();
}