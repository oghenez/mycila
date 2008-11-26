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
package com.mycila.testing.core;

import com.mycila.plugin.api.PluginBinding;
import com.mycila.plugin.api.PluginResolver;
import com.mycila.plugin.spi.PluginManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class TestContext implements Context {

    private final Object testInstance;
    private final Map<String, Object> attributes = new HashMap<String, Object>();
    private final PluginManager<TestPlugin> pluginManager;

    TestContext(PluginManager<TestPlugin> pluginManager, Object testInstance) {
        this.testInstance = testInstance;
        this.pluginManager = pluginManager;
    }

    public PluginResolver<TestPlugin> getPluginResolver() {
        return pluginManager.getResolver();
    }

    public <T> T getAttribute(String name) {
        //noinspection unchecked
        T att = (T) attributes.get(name);
        if (att == null) {
            throw new TestPluginException("Inexisting attribute: '%s'", name);
        }
        return att;
    }

    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public Object getTestInstance() {
        return testInstance;
    }

    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    void execute() throws TestPluginException {
        for (PluginBinding<TestPlugin> binding : getPluginResolver().getResolvedPlugins()) {
            try {
                binding.getPlugin().prepareTestInstance(this);
            } catch (Exception e) {
                throw new TestPluginException("An error occured while executing plugin '%s': %s", e, binding.getName(), e.getMessage());
            }
        }
    }
}
