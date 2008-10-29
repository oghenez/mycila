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
        repo = new ClasspathPluginRepository<MyPlugin>(MyPlugin.class, "/com/google/code/plugin/empty.properties");
        assertEquals(repo.loadPlugins().size(), 0);
    }

    @Test
    public void test_load_ok() {
        ClasspathPluginRepository<MyPlugin> repo = new ClasspathPluginRepository<MyPlugin>(MyPlugin.class, "/com/google/code/plugin/two.properties");
        assertEquals(repo.loadPlugins().size(), 2);
    }

    @Test
    public void test_load_other_cp() {
        ClasspathPluginRepository<MyPlugin> repo = new ClasspathPluginRepository<MyPlugin>(MyPlugin.class, "/com/google/code/plugin/two.properties");
        repo.setLoader(new URLClassLoader(new URL[0], null));
        assertEquals(repo.loadPlugins().size(), 0);
    }

    @Test(expectedExceptions = PluginLoadException.class)
    public void test_bad_class() {
        ClasspathPluginRepository<MyPlugin> repo = new ClasspathPluginRepository<MyPlugin>(MyPlugin.class, "/com/google/code/plugin/badClass.properties");
        repo.loadPlugins();
    }

    @Test(expectedExceptions = PluginLoadException.class)
    public void test_not_plugin_class() {
        ClasspathPluginRepository<MyPlugin> repo = new ClasspathPluginRepository<MyPlugin>(MyPlugin.class, "com/google/code/plugin/notPlugin.properties");
        repo.loadPlugins();
    }

    @Test(expectedExceptions = PluginLoadException.class)
    public void test_cannot_instanciate() {
        ClasspathPluginRepository<MyPlugin> repo = new ClasspathPluginRepository<MyPlugin>(MyPlugin.class, "com/google/code/plugin/instance.properties");
        repo.loadPlugins();
    }

    @Test(expectedExceptions = DuplicatePluginException.class)
    public void test_duplicate() throws Exception {
        ClasspathPluginRepository<MyPlugin> repo = new ClasspathPluginRepository<MyPlugin>(MyPlugin.class, "duplicate.properties");
        repo.setLoader(new URLClassLoader(new URL[]{
                new File("src/test/data").toURI().toURL(),
                new File("src/test/resources/com/google/code/plugin").toURI().toURL(),
        }));
        repo.loadPlugins();
    }

    @Test
    public void test_exclude() throws Exception {
        ClasspathPluginRepository<MyPlugin> repo = new ClasspathPluginRepository<MyPlugin>(MyPlugin.class, "duplicate.properties");
        repo.setLoader(new URLClassLoader(new URL[]{
                new File("src/test/data").toURI().toURL(),
                new File("src/test/resources/com/google/code/plugin").toURI().toURL(),
        }));
        repo.setExclusions("plugin1");
        assertEquals(repo.loadPlugins().size(), 1);
    }
}
