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

import com.mycila.log.Logger;
import com.mycila.log.Loggers;
import com.mycila.plugin.api.PluginBinding;
import com.mycila.plugin.spi.PluginManager;
import static com.mycila.testing.core.api.Ensure.*;
import com.mycila.testing.core.api.Step;
import com.mycila.testing.core.api.TestContext;
import com.mycila.testing.core.api.TestNotifier;
import com.mycila.testing.core.api.TestPluginException;
import com.mycila.testing.core.introspect.Introspector;
import com.mycila.testing.core.plugin.TestPlugin;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class TestContextImpl implements TestContext, TestNotifier {

    private static final Method prepareMethod;static {
        try {
            prepareMethod = TestNotifier.class.getDeclaredMethod("prepare");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static final Method fireAfterClassMethod;static {
        try {
            fireAfterClassMethod = TestNotifier.class.getDeclaredMethod("fireAfterClass");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static final Logger LOGGER = Loggers.get(TestContextImpl.class);

    private final Introspector introspector;
    private final Map<String, Object> attributes = new HashMap<String, Object>();
    private final PluginManager<TestPlugin> pluginManager;

    TestContextImpl(PluginManager<TestPlugin> pluginManager, Object testInstance) {
        notNull("Plugin manager", pluginManager);
        notNull("Test instance", testInstance);
        this.introspector = new Introspector(testInstance);
        this.pluginManager = pluginManager;
        LOGGER.debug("Creating new Test Context for test {0}#{1,number,#}", this.introspector.testClass().getName(), this.introspector.instance().hashCode());
        Mycila.registerContext(this);
    }

    @SuppressWarnings({"unchecked"})
    public <T> T attribute(String name) {
        notNull("Attribute name", introspector);
        T att = (T) attributes.get(name);
        if (att == null) {
            throw new TestPluginException("Inexisting attribute: '%s'", name);
        }
        return att;
    }

    public PluginManager<TestPlugin> pluginManager() {
        return pluginManager;
    }

    public Map<String, Object> attributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public Introspector introspector() {
        return introspector;
    }

    public boolean hasAttribute(String name) {
        notNull("Attribute name", introspector);
        return attributes.containsKey(name);
    }

    public void setAttribute(String name, Object value) {
        notNull("Attribute name", name);
        attributes.put(name, value);
    }

    @SuppressWarnings({"unchecked"})
    public <T> T removeAttribute(String name) {
        notNull("Attribute name", name);
        return (T) attributes.remove(name);
    }

    public void prepare() throws TestPluginException {
        try {
            ExecutionImpl execution = new ExecutionImpl(this, prepareMethod);
            Mycila.registerCurrentExecution(execution.changeStep(Step.PREPARE));
            LOGGER.debug("Calling 'prepareTestInstance' on plugins for test {0}#{1,number,#}...", introspector.testClass().getName(), introspector.instance().hashCode());
            for (PluginBinding<TestPlugin> binding : pluginManager.getResolver().getResolvedPlugins()) {
                try {
                    binding.getPlugin().prepareTestInstance(this);
                } catch (Exception e) {
                    throw new TestPluginException(e, "An error occured while executing 'prepareTestInstance' on plugin '%s': %s: %s", binding.getName(), e.getClass().getSimpleName(), e.getMessage());
                }
            }
        } finally {
            Mycila.unsetCurrentExecution();
        }
    }

    public void fireBeforeTest(Method method) throws TestPluginException {
        notNull("Test method", method);
        TestExecutionImpl testExecution = new TestExecutionImpl(this, method);
        try {
            Mycila.registerCurrentExecution(testExecution.changeStep(Step.BEFORE));
            LOGGER.debug("Calling 'beforeTest' on plugins for test {0}#{1,number,#}...", introspector.testClass().getName(), introspector.instance().hashCode());
            for (PluginBinding<TestPlugin> binding : pluginManager.getResolver().getResolvedPlugins()) {
                try {
                    binding.getPlugin().beforeTest(testExecution);
                } catch (Exception e) {
                    throw new TestPluginException(e, "An error occured while executing 'beforeTest' on plugin '%s': %s: %s", binding.getName(), e.getClass().getSimpleName(), e.getMessage());
                }
            }
        } finally {
            Mycila.unsetCurrentExecution();
            Mycila.registerCurrentExecution(testExecution.changeStep(Step.TEST));
        }
    }

    public void fireAfterTest() throws TestPluginException {
        try {
            TestExecutionImpl testExecution = (TestExecutionImpl) Mycila.currentExecution();
            Mycila.unsetCurrentExecution();
            Mycila.registerCurrentExecution(testExecution.changeStep(Step.AFTER));
            LOGGER.debug("Calling 'afterTest' on plugins for test {0}#{1,number,#}...", introspector.testClass().getName(), introspector.instance().hashCode());
            for (PluginBinding<TestPlugin> binding : pluginManager.getResolver().getResolvedPlugins()) {
                try {
                    binding.getPlugin().afterTest(testExecution);
                } catch (Exception e) {
                    throw new TestPluginException(e, "An error occured while executing 'afterTest' on plugin '%s': %s: %s", binding.getName(), e.getClass().getSimpleName(), e.getMessage());
                }
            }
        } finally {
            Mycila.unsetCurrentExecution();
        }
    }

    public void fireAfterClass() throws TestPluginException {
        try {
            ExecutionImpl execution = new ExecutionImpl(this, fireAfterClassMethod);
            Mycila.registerCurrentExecution(execution.changeStep(Step.COMPLETED));
            LOGGER.debug("Calling 'afterClass' on plugins for test {0}#{1,number,#}...", introspector.testClass().getName(), introspector.instance().hashCode());
            for (PluginBinding<TestPlugin> binding : pluginManager.getResolver().getResolvedPlugins()) {
                try {
                    binding.getPlugin().afterClass(this);
                } catch (Exception e) {
                    throw new TestPluginException(e, "An error occured while executing 'afterClass' on plugin '%s': %s: %s", binding.getName(), e.getClass().getSimpleName(), e.getMessage());
                }
            }
        } finally {
            Mycila.unsetCurrentExecution();
        }
    }

}
