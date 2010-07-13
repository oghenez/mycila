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

import java.lang.reflect.Member;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class MissingScopeParameterException extends PluginScopeException {
    private static final long serialVersionUID = 1;

    public MissingScopeParameterException(Member member, Class<? extends ScopeProvider> scopeClass, String parameterName) {
        super("Scope parameter '" + parameterName + "' is missing at member " + member + " for scope " + scopeClass.getSimpleName());
    }
}
