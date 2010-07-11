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

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class PluginIOException extends PluginException {
    private static final long serialVersionUID = 5781552689799348255L;

    public PluginIOException(Throwable t, String message, Object... args) {
        super(format("%s: %s", format(message, args), t.getMessage()), t);
    }
}