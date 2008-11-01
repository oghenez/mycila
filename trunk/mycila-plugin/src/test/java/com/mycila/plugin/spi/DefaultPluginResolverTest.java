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
    public void test_getMissingDependencies() {
        setupResolver("/com/mycila/plugin/spi/two.properties");
        assertFalse(resolver.getMissingDependencies().isEmpty());

        setupResolver("/com/mycila/plugin/spi/missing.properties");
        assertEquals(resolver.getMissingDependencies().size(), 2);
        assertEquals(resolver.getMissingDependencies().firstKey(), "plugin2");
        assertEquals(resolver.getMissingDependencies().get("plugin2").get(0), "inexisting");
        assertEquals(resolver.getMissingDependencies().get("plugin4").get(0), "inexisting");
    }

    @Test
    public void test_getResolvedPluginsName_empty() {
        setupResolver("/com/mycila/plugin/spi/empty.properties");
        assertEquals(resolver.getResolvedPluginsName().size(), 0);
    }

    @Test
    public void test_getResolvedPluginsName() {
        setupResolver("/com/mycila/plugin/spi/two.properties");
        assertEquals(resolver.getResolvedPluginsName().size(), 2);
        assertListEquals(resolver.getResolvedPluginsName(), "plugin2", "plugin1");
    }

    @Test
    public void test_getResolvedPluginsName_complex() {
        setupResolver("/com/mycila/plugin/spi/complex.properties");
        assertEquals(resolver.getMissingDependencies().firstKey(), "plugin4");
        assertEquals(resolver.getMissingDependencies().values().iterator().next().get(0), "inexisting");
        assertEquals(resolver.getResolvedPluginsName().size(), 4);
        assertListEquals(resolver.getResolvedPluginsName(), "plugin5", "plugin4", "plugin2", "plugin1");
    }

    @Test
    public void test_getResolvedPluginsName_cyclomatic() {
        try {
            setupResolver("/com/mycila/plugin/spi/cyclo.properties");
            resolver.getResolvedPluginsName();
            fail("must throw CyclicDependencyException");
        } catch (CyclicDependencyException e) {
            assertEquals(e, "");
        }
    }

    @Test
    public void test_getResolvedPlugins() {
        setupResolver("/com/mycila/plugin/spi/four.properties");
        assertListEquals(resolver.getResolvedPluginsName(), "plugin5", "plugin4", "plugin2", "plugin1");
        List<MyPlugin> plugins = resolver.getResolvedPlugins();
        assertEquals(plugins.get(0).getClass(), MyPlugin5.class);
        assertEquals(plugins.get(2).getClass(), MyPlugin4.class);
        assertEquals(plugins.get(3).getClass(), MyPlugin2.class);
        assertEquals(plugins.get(4).getClass(), MyPlugin1.class);
    }

    private void setupResolver(String descriptor) {
        resolver = new DefaultPluginResolver<MyPlugin>(new DefaultPluginCache<MyPlugin>(new DefaultPluginLoader<MyPlugin>(MyPlugin.class, descriptor)));
    }

    private void assertListEquals(List<String> plugins, String... names) {
        for (int i = 0; i < names.length; i++) {
            assertEquals(plugins.get(i), names[i], "\nResolved: " + plugins + "\nExpected: " + Arrays.deepToString(names));
        }
    }

}