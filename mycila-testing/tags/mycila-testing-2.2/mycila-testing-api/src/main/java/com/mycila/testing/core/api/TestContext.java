/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
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

package com.mycila.testing.core.api;

import com.mycila.plugin.spi.PluginManager;
import com.mycila.testing.core.introspect.Introspector;
import com.mycila.testing.core.plugin.TestPlugin;

/**
 * A context handles plugin management and hooks for a test.
 * It can be used to put any attributes which are only available per test instance.
 *
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface TestContext {

    /**
     * @return The plugin manager used for this test
     */
    PluginManager<TestPlugin> pluginManager();

    /**
     * Get the representation of this test
     *
     * @return A {@link com.mycila.testing.core.introspect.Introspector} instance for this test
     */
    Introspector introspector();

    /**
     * Handle attributes of this Test Context, which can be shared amongst plugins
     *
     * @return Attribute handler
     */
    Attributes attributes();
}
