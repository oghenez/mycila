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

package com.mycila.plugin;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class ClasspathPluginRepositoryTest {

    @Test
    public void test_load_inexisting() {
        ClasspathPluginRepository<MyPlugin> repo = new ClasspathPluginRepository<MyPlugin>(MyPlugin.class, "/inexisting.properties");
        assertEquals(repo.getDescriptor(), "inexisting.properties");
        assertEquals(repo.loadPlugins().size(), 0);
        repo = new ClasspathPluginRepository<MyPlugin>(MyPlugin.class, "/com/mycila/plugin/empty.properties");
        assertEquals(repo.loadPlugins().size(), 0);
    }

    @Test
    public void test_load_ok() {
        ClasspathPluginRepository<MyPlugin> repo = new ClasspathPluginRepository<MyPlugin>(MyPlugin.class, "/com/mycila/plugin/two.properties");
        assertEquals(repo.loadPlugins().size(), 2);
    }

    @Test
    public void test_load_other_cp() {
        ClasspathPluginRepository<MyPlugin> repo = new ClasspathPluginRepository<MyPlugin>(MyPlugin.class, "/com/mycila/plugin/two.properties");
        repo.setLoader(new URLClassLoader(new URL[0], null));
        assertEquals(repo.loadPlugins().size(), 0);
    }

    @Test(expectedExceptions = PluginLoadException.class)
    public void test_bad_class() {
        ClasspathPluginRepository<MyPlugin> repo = new ClasspathPluginRepository<MyPlugin>(MyPlugin.class, "/com/mycila/plugin/badClass.properties");
        repo.loadPlugins();
    }

    @Test(expectedExceptions = PluginLoadException.class)
    public void test_not_plugin_class() {
        ClasspathPluginRepository<MyPlugin> repo = new ClasspathPluginRepository<MyPlugin>(MyPlugin.class, "com/mycila/plugin/notPlugin.properties");
        repo.loadPlugins();
    }

    @Test(expectedExceptions = PluginLoadException.class)
    public void test_cannot_instanciate() {
        ClasspathPluginRepository<MyPlugin> repo = new ClasspathPluginRepository<MyPlugin>(MyPlugin.class, "com/mycila/plugin/instance.properties");
        repo.loadPlugins();
    }

    @Test(expectedExceptions = DuplicatePluginException.class)
    public void test_duplicate() throws Exception {
        ClasspathPluginRepository<MyPlugin> repo = new ClasspathPluginRepository<MyPlugin>(MyPlugin.class, "duplicate.properties");
        repo.setLoader(new URLClassLoader(new URL[]{
                new File("src/test/data").toURI().toURL(),
                new File("src/test/resources/com/mycila/plugin").toURI().toURL(),
        }));
        repo.loadPlugins();
    }

    @Test
    public void test_exclude() throws Exception {
        ClasspathPluginRepository<MyPlugin> repo = new ClasspathPluginRepository<MyPlugin>(MyPlugin.class, "duplicate.properties");
        repo.setLoader(new URLClassLoader(new URL[]{
                new File("src/test/data").toURI().toURL(),
                new File("src/test/resources/com/mycila/plugin").toURI().toURL(),
        }));
        repo.setExclusions("plugin1");
        assertEquals(repo.loadPlugins().size(), 1);
    }
}
