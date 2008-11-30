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

import com.mycila.plugin.api.DuplicatePluginException;
import com.mycila.plugin.api.PluginCreationException;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DefaultPluginLoaderTest {

    @Test
    public void test_load_inexisting() {
        DefaultPluginLoader<MyPlugin> repo = new DefaultPluginLoader<MyPlugin>(MyPlugin.class, "/inexisting.properties");
        assertEquals(repo.descriptor, "inexisting.properties");
        assertEquals(repo.loadPlugins().size(), 0);
        repo = new DefaultPluginLoader<MyPlugin>(MyPlugin.class, "/com/mycila/plugin/spi/empty.properties");
        assertEquals(repo.loadPlugins().size(), 0);
    }

    @Test
    public void test_load_ok() {
        DefaultPluginLoader<MyPlugin> repo = new DefaultPluginLoader<MyPlugin>(MyPlugin.class, "/com/mycila/plugin/spi/two.properties");
        assertEquals(repo.loadPlugins().size(), 2);
    }

    @Test
    public void test_load_other_cp() {
        DefaultPluginLoader<MyPlugin> repo = new DefaultPluginLoader<MyPlugin>(MyPlugin.class, "/com/mycila/plugin/spi/two.properties");
        repo.setLoader(new URLClassLoader(new URL[0], null));
        assertEquals(repo.loadPlugins().size(), 0);
    }

    @Test
    public void test_bad_class() {
        try {
            DefaultPluginLoader<MyPlugin> repo = new DefaultPluginLoader<MyPlugin>(MyPlugin.class, "/com/mycila/plugin/spi/badClass.properties");
            repo.loadPlugins();
            fail("must throw PluginCreationException");
        } catch (PluginCreationException e) {
            assertEquals(e.getClazz(), "#$#$#$");
            assertEquals(e.getDescriptor(), getClass().getResource("/com/mycila/plugin/spi/badClass.properties"));
            assertEquals(e.getPlugin(), "plugin1");
            assertEquals(e.getPluginType(), MyPlugin.class);
        }
    }

    @Test
    public void test_not_plugin_class() {
        try {
            DefaultPluginLoader<MyPlugin> repo = new DefaultPluginLoader<MyPlugin>(MyPlugin.class, "/com/mycila/plugin/spi/notPlugin.properties");
            repo.loadPlugins();
            fail("must throw PluginCreationException");
        } catch (PluginCreationException e) {
            assertEquals(e.getClazz(), "java.lang.String");
            assertEquals(e.getDescriptor(), getClass().getResource("/com/mycila/plugin/spi/notPlugin.properties"));
            assertEquals(e.getPlugin(), "plugin1");
            assertEquals(e.getPluginType(), MyPlugin.class);
        }
    }

    @Test
    public void test_cannot_instanciate() {
        try {
            DefaultPluginLoader<MyPlugin> repo = new DefaultPluginLoader<MyPlugin>(MyPlugin.class, "/com/mycila/plugin/spi/instance.properties");
            repo.loadPlugins();
            fail("must throw PluginCreationException");
        } catch (PluginCreationException e) {
            assertEquals(e.getClazz(), "com.mycila.plugin.spi.PrivatePlugin");
            assertEquals(e.getDescriptor(), getClass().getResource("/com/mycila/plugin/spi/instance.properties"));
            assertEquals(e.getPlugin(), "plugin3");
            assertEquals(e.getPluginType(), MyPlugin.class);
        }
    }

    @Test
    public void test_duplicate() throws Exception {
        try {
            DefaultPluginLoader<MyPlugin> repo = new DefaultPluginLoader<MyPlugin>(MyPlugin.class, "duplicate.properties");
            repo.setLoader(new URLClassLoader(new URL[]{
                    new File("src/test/data").toURI().toURL(),
                    new File("src/test/resources/com/mycila/plugin/spi").toURI().toURL(),
            }));
            repo.loadPlugins();
            fail("must throw DuplicatePluginException");
        } catch (DuplicatePluginException e) {
            assertEquals(e.getDescriptor(), new File("src/test/resources/com/mycila/plugin/spi/duplicate.properties").toURI().toURL());
            assertEquals(e.getPlugin(), "plugin1");
        }
    }

    @Test
    public void test_exclude() throws Exception {
        DefaultPluginLoader<MyPlugin> repo = new DefaultPluginLoader<MyPlugin>(MyPlugin.class, "duplicate.properties");
        repo.setLoader(new URLClassLoader(new URL[]{
                new File("src/test/data").toURI().toURL(),
                new File("src/test/resources/com/mycila/plugin/spi").toURI().toURL(),
        }));
        repo.setExclusions("plugin1");
        assertEquals(repo.loadPlugins().size(), 1);
    }
}
