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

    void execute() {
        for (PluginBinding<TestPlugin> binding : getPluginResolver().getResolvedPlugins()) {
            binding.getPlugin().prepareTestInstance(this);
        }
    }
}
