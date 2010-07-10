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

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class PluginManagerTest {

    @Test
    public void test_create() {
        PluginManager<MyPlugin> manager = new PluginManager<MyPlugin>(MyPlugin.class, "/com/mycila/plugin/spi/complex1.properties");
        manager.getLoader().setExclusions("plugin1");
        manager.getCache().registerPlugin("plugin1", new MyPlugin4());
        List<String> list = manager.getResolver().getResolvedPluginsName();
        assertEquals(list.size(), 6);
        assertListEquals(list, "plugin2", "plugin1", "plugin5", "plugin6", "plugin3", "plugin4");
    }

    @Test
    public void test_create2() {
        PluginManager<MyPlugin> manager = new PluginManager<MyPlugin>(MyPlugin.class);
        List<String> list = manager.getResolver().getResolvedPluginsName();
        assertEquals(list.size(), 0);
    }

    @Test
    public void test_with_null() {
        PluginManager<MyPlugin> manager = new PluginManager<MyPlugin>(MyPlugin.class);
        manager.getCache().registerPlugin("heho", new MyPlugin() {
            public void execute() {
            }

            public List<String> getBefore() {
                return null;
            }

            public List<String> getAfter() {
                return null;
            }
        });
        List<String> list = manager.getResolver().getResolvedPluginsName();
        assertEquals(list.size(), 1);
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
