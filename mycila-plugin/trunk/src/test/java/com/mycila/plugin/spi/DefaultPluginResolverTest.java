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

import com.mycila.plugin.api.*;
import static com.mycila.plugin.spi.Builder.*;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DefaultPluginResolverTest {

    PluginResolver<MyPlugin> resolver;
    private PluginCache<MyPlugin> cache;

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
        List<String> list = resolver.getResolvedPluginsName();
        assertEquals(list.size(), 4);
        assertListEquals(list, "inexisting", "plugin2", "plugin1", "plugin3");
    }

    @Test
    public void test_getResolvedPluginsName_complex1() {
        setupResolver("/com/mycila/plugin/spi/complex1.properties");
        List<String> list = resolver.getResolvedPluginsName();
        assertEquals(list.size(), 6);
        assertListEquals(list, "plugin2", "plugin1", "plugin5", "plugin6", "plugin3", "plugin4");
    }

    @Test
    public void test_getResolvedPluginsName_complex2() {
        setupResolver();

        cache.registerPlugin("plugin1", create().build());
        cache.registerPlugin("plugin2", create().afters("plugin1").build());
        cache.registerPlugin("plugin3", create().befores("plugin1").build());
        cache.registerPlugin("plugin4", create().build());
        cache.registerPlugin("plugin5", create().befores("plugin1").afters("plugin4").build());
        cache.registerPlugin("plugin6", create().befores("plugin5", "plugin2").afters("plugin3", "plugin4").build());

        List<String> list = resolver.getResolvedPluginsName();
        assertListEquals(list, "plugin2", "plugin1", "plugin5", "plugin6", "plugin3", "plugin4");
    }

    @Test
    public void test_getResolvedPluginsName_complex3() {
        setupResolver();

        cache.registerPlugin("plugin0", create().build());
        cache.registerPlugin("plugin1", create().build());
        cache.registerPlugin("plugin2", create().afters("plugin1").build());
        cache.registerPlugin("plugin3", create().befores("plugin1").build());

        List<String> list = resolver.getResolvedPluginsName();
        assertListEquals(list, "plugin0", "plugin2", "plugin1", "plugin3");
    }

    @Test
    public void test_getResolvedPluginsName_complex4() {
        setupResolver();

        cache.registerPlugin("plugin0", create().build());
        cache.registerPlugin("plugin1", create().build());
        cache.registerPlugin("plugin2", create().build());
        cache.registerPlugin("plugin3", create().build());
        cache.registerPlugin("plugin4", create().afters("plugin0").befores("plugin2").build());

        List<String> list = resolver.getResolvedPluginsName();
        assertListEquals(list, "plugin1", "plugin2", "plugin3", "plugin4", "plugin0");
    }

    @Test
    public void test_getResolvedPluginsName_complex5() {
        setupResolver();

        cache.registerPlugin("plugin0", create().build());
        cache.registerPlugin("plugin1", create().build());
        cache.registerPlugin("plugin2", create().build());
        cache.registerPlugin("plugin4", create().afters("plugin1").befores("plugin2").build());
        cache.registerPlugin("plugin3", create().befores("plugin4", "plugin0").afters("plugin1").build());

        List<String> list = resolver.getResolvedPluginsName();
        assertListEquals(list, "plugin0", "plugin2", "plugin4", "plugin3", "plugin1");
    }

    @Test
    public void test_getResolvedPluginsName_complex6() {
        setupResolver();

        cache.registerPlugin("plugin0", create().build());
        cache.registerPlugin("plugin1", create().build());
        cache.registerPlugin("plugin2", create().afters("plugin1").build());
        cache.registerPlugin("plugin3", create().befores("plugin1").afters("plugin0").build());

        List<String> list = resolver.getResolvedPluginsName();
        assertListEquals(list, "plugin2", "plugin1", "plugin3", "plugin0");
    }

    @Test
    public void test_getResolvedPluginsName_complex7() {
        setupResolver();

        cache.registerPlugin("plugin0", create().build());
        cache.registerPlugin("plugin1", create().build());
        cache.registerPlugin("pluginX", create().build());
        cache.registerPlugin("plugin2", create().afters("plugin1").build());
        cache.registerPlugin("plugin3", create().befores("plugin0").build());
        cache.registerPlugin("plugin4", create().afters("plugin0").build());

        List<String> list = resolver.getResolvedPluginsName();
        assertListEquals(list, "plugin2", "plugin4", "pluginX", "plugin1", "plugin0", "plugin3");
    }

    @Test
    public void test_getResolvedPluginsName_cyclic() {
        try {
            setupResolver();

            cache.registerPlugin("plugin0", create().build());
            cache.registerPlugin("plugin1", create().afters("plugin0").build());
            cache.registerPlugin("plugin2", create().afters("plugin1").befores("plugin0").build());

            List<String> list = resolver.getResolvedPluginsName();
            fail("must throw CyclicDependencyException. Actual: " + list);
        } catch (CyclicDependencyException e) {
            System.out.println(e.getMessage());
            assertEquals(e.getCyclics().keySet().toString(), "[plugin0, plugin1, plugin2]");
        }
    }

    @Test
    public void test_getResolvedPluginsName_cyclic2() {
        try {
            setupResolver();

            cache.registerPlugin("plugin0", create().build());
            cache.registerPlugin("plugin1", create().afters("plugin0").build());
            cache.registerPlugin("plugin2", create().afters("plugin1").befores("plugin3").build());
            cache.registerPlugin("plugin3", create().befores("plugin0").build());

            List<String> list = resolver.getResolvedPluginsName();
            fail("must throw CyclicDependencyException. Actual: " + list);
        } catch (CyclicDependencyException e) {
            System.out.println(e.getMessage());
            assertEquals(e.getCyclics().keySet().toString(), "[plugin0, plugin1, plugin2, plugin3]");
        }
    }

    @Test
    public void test_getResolvedPlugins() {
        setupResolver("/com/mycila/plugin/spi/noMiss.properties");
        List<PluginBinding<MyPlugin>> plugins = resolver.getResolvedPlugins();
        assertEquals(plugins.get(0).getPlugin().getClass(), MyPlugin4.class);
        assertEquals(plugins.get(1).getPlugin().getClass(), MyPlugin2.class);
        assertEquals(plugins.get(2).getPlugin().getClass(), MyPlugin1.class);
        assertEquals(plugins.get(3).getPlugin().getClass(), MyPlugin3.class);
    }

    private void setupResolver(String descriptor) {
        cache = new DefaultPluginCache<MyPlugin>(new DefaultPluginLoader<MyPlugin>(MyPlugin.class, descriptor));
        resolver = new DefaultPluginResolver<MyPlugin>(cache);
    }

    private void setupResolver() {
        cache = new DefaultPluginCache<MyPlugin>(new DefaultPluginLoader<MyPlugin>(MyPlugin.class));
        resolver = new DefaultPluginResolver<MyPlugin>(cache);
    }

    private void assertListEquals(List<String> plugins, String... names) {
        if (plugins.size() != names.length) {
            fail("Size differs.\nResolved: " + plugins + "\nExpected: " + Arrays.deepToString(names));
        }
        for (int i = 0; i < plugins.size() && i < names.length; i++) {
            assertEquals(plugins.get(i), names[i], "\nResolved: " + plugins + "\nExpected: " + Arrays.deepToString(names));
        }
    }

}