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

package com.mycila.plugin.spi;

import com.mycila.plugin.api.CyclicDependencyException;
import com.mycila.plugin.api.InexistingPluginException;
import com.mycila.plugin.api.PluginResolver;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DefaultPluginResolverTest {

    PluginResolver<MyPlugin> resolver;

    @Test
    public void test_getPlugins() {
        setupResolver("/com/mycila/plugin/spi/two.properties");
        assertEquals(resolver.getPlugins().size(), 2);
    }

    @Test
    public void test_getPlugin() {
        setupResolver("/com/mycila/plugin/spi/two.properties");
        assertNotNull(resolver.getPlugin("plugin1"));
    }

    @Test
    public void test_getPlugin_error() {
        try {
            setupResolver("/com/mycila/plugin/spi/two.properties");
            resolver.getPlugin("plugin3");
            fail("must throw InexistingPluginException");
        } catch (InexistingPluginException e) {
            assertEquals(e.getPlugin(), "plugin3");
        }
    }

    @Test
    public void test_getPlugins_name() {
        setupResolver("/com/mycila/plugin/spi/two.properties");
        assertEquals(resolver.getPlugins("plugin1", "plugin1").size(), 2);
    }

    @Test
    public void test_getPlugins_error() {
        try {
            setupResolver("/com/mycila/plugin/spi/two.properties");
            resolver.getPlugins("plugin1", "plugin2", "plugin3");
            fail("must throw InexistingPluginException");
        } catch (InexistingPluginException e) {
            assertEquals(e.getPlugin(), "plugin3");
        }
    }

    @Test
    public void test_contains() {
        setupResolver("/com/mycila/plugin/spi/two.properties");
        assertTrue(resolver.contains("plugin1"));
        assertFalse(resolver.contains("plugin3"));
    }

    @Test
    public void test_getMissingDependenciesByPlugin() {
        setupResolver("/com/mycila/plugin/spi/noMiss.properties");
        assertTrue(resolver.getMissingDependenciesByPlugin().isEmpty());

        setupResolver("/com/mycila/plugin/spi/two.properties");
        assertFalse(resolver.getMissingDependenciesByPlugin().isEmpty());
        assertEquals(resolver.getMissingDependenciesByPlugin().size(), 2);
        assertEquals(resolver.getMissingDependenciesByPlugin().firstKey(), "plugin1");
        assertEquals(resolver.getMissingDependenciesByPlugin().get("plugin1").first(), "plugin3");
        assertEquals(resolver.getMissingDependenciesByPlugin().get("plugin2").first(), "inexisting");
    }

    @Test
    public void test_getResolvedPluginsName_empty() {
        setupResolver("/com/mycila/plugin/spi/empty.properties");
        assertEquals(resolver.getResolvedPluginsName().size(), 0);
    }

    @Test
    public void test_getResolvedPluginsName_simple() {
        setupResolver("/com/mycila/plugin/spi/two.properties");
        assertEquals(resolver.getResolvedPluginsName().size(), 2);
        assertListEquals(resolver.getResolvedPluginsName(), "plugin2", "plugin1");
    }

    @Test
    public void test_getResolvedPluginsName_simple_noMiss() {
        setupResolver("/com/mycila/plugin/spi/noMiss.properties");
        assertEquals(resolver.getResolvedPluginsName().size(), 4);
        assertListEquals(resolver.getResolvedPluginsName(), "inexisting", "plugin2", "plugin1", "plugin3");
    }

    @Test
    public void test_getResolvedPluginsName_complex() {
        setupResolver("/com/mycila/plugin/spi/complex.properties");
        List<String> list = resolver.getResolvedPluginsName();
        assertEquals(list.size(), 6);
        assertListEquals(list, "plugin2", "plugin1", "plugin5", "plugin6", "plugin4", "plugin3");
    }

    @Test
    public void test_getResolvedPluginsName_cyclomatic() {
        try {
            setupResolver("/com/mycila/plugin/spi/cyclo.properties");
            List<String> list = resolver.getResolvedPluginsName();
            fail("must throw CyclicDependencyException. Actual: " + list);
        } catch (CyclicDependencyException e) {
            assertEquals(e.getPluginName(), "plugin7");
            assertEquals(e.getPluginsNames().toString(), "[plugin2, plugin1, plugin5, plugin6, plugin3]");
            assertEquals(e.getInsertionIndex(), 3);
            assertEquals(e.getPlugin().getClass(), MyPlugin7.class);
        }
    }

    @Test
    public void test_getResolvedPlugins() {
        setupResolver("/com/mycila/plugin/spi/noMiss.properties");
        List<MyPlugin> plugins = resolver.getResolvedPlugins();
        assertEquals(plugins.get(0).getClass(), MyPlugin4.class);
        assertEquals(plugins.get(1).getClass(), MyPlugin2.class);
        assertEquals(plugins.get(2).getClass(), MyPlugin1.class);
        assertEquals(plugins.get(3).getClass(), MyPlugin3.class);
    }

    private void setupResolver(String descriptor) {
        resolver = new DefaultPluginResolver<MyPlugin>(new DefaultPluginCache<MyPlugin>(new DefaultPluginLoader<MyPlugin>(MyPlugin.class, descriptor)));
    }

    private void assertListEquals(List<String> plugins, String... names) {
        if (plugins.size() != names.length) {
            fail("Sizes differs.\nResolved: " + plugins + "\nExpected: " + Arrays.deepToString(names));
        }
        for (int i = 0; i < plugins.size() && i < names.length; i++) {
            assertEquals(plugins.get(i), names[i], "\nResolved: " + plugins + "\nExpected: " + Arrays.deepToString(names));
        }
    }

}