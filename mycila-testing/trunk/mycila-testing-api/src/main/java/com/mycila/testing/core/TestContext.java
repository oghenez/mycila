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
import com.mycila.plugin.spi.PluginManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class TestContext implements Context, TestHandler {

    private final TestInstance testInstance;
    private final Map<String, Object> attributes = new HashMap<String, Object>();
    private final PluginManager<TestPlugin> pluginManager;

    TestContext(PluginManager<TestPlugin> pluginManager, Object testInstance) {
        this.testInstance = new TestInstance(testInstance);
        this.pluginManager = pluginManager;
    }

    @SuppressWarnings({"unchecked"})
    public <T> T getAttribute(String name) {
        T att = (T) attributes.get(name);
        if (att == null) {
            throw new TestPluginException("Inexisting attribute: '%s'", name);
        }
        return att;
    }

    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public TestInstance getTest() {
        return testInstance;
    }

    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    @SuppressWarnings({"unchecked"})
    public <T> T removeAttribute(String name) {
        return (T) attributes.remove(name);
    }

    public void prepare() throws TestPluginException {
        try {
            ContextHolder.set(this);
            for (PluginBinding<TestPlugin> binding : pluginManager.getResolver().getResolvedPlugins()) {
                try {
                    binding.getPlugin().prepareTestInstance(this);
                } catch (Exception e) {
                    throw new TestPluginException(e, "An error occured while executing 'prepareTestInstance' on plugin '%s': %s: %s", binding.getName(), e.getClass().getSimpleName(), e.getMessage());
                }
            }
        } finally {
            ContextHolder.unset();
        }
    }

    public boolean beforeTest(Method method) throws TestPluginException {
        boolean b = true;
        try {
            ContextHolder.set(this);
            for (PluginBinding<TestPlugin> binding : pluginManager.getResolver().getResolvedPlugins()) {
                try {
                    b &= binding.getPlugin().beforeTest(this, method);
                } catch (Exception e) {
                    throw new TestPluginException(e, "An error occured while executing 'beforeTest' on plugin '%s': %s: %s", binding.getName(), e.getClass().getSimpleName(), e.getMessage());
                }
            }
        } finally {
            ContextHolder.unset();
        }
        return b;
    }

    public void afterTest(Method method, Throwable throwable) throws TestPluginException {
        try {
            if (throwable != null && throwable instanceof InvocationTargetException) {
                throwable = ((InvocationTargetException) throwable).getTargetException();
            }
            ContextHolder.set(this);
            for (PluginBinding<TestPlugin> binding : pluginManager.getResolver().getResolvedPlugins()) {
                try {
                    binding.getPlugin().afterTest(this, method, throwable);
                } catch (Exception e) {
                    throw new TestPluginException(e, "An error occured while executing 'afterTest' on plugin '%s': %s: %s", binding.getName(), e.getClass().getSimpleName(), e.getMessage());
                }
            }
        } finally {
            ContextHolder.unset();
        }
    }

    public void end() throws TestPluginException {
        try {
            ContextHolder.set(this);
            for (PluginBinding<TestPlugin> binding : pluginManager.getResolver().getResolvedPlugins()) {
                try {
                    binding.getPlugin().afterClass(this);
                } catch (Exception e) {
                    throw new TestPluginException(e, "An error occured while executing 'afterClass' on plugin '%s': %s: %s", binding.getName(), e.getClass().getSimpleName(), e.getMessage());
                }
            }
        } finally {
            ContextHolder.unset();
        }
    }

}
