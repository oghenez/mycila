package com.mycila.plugin.spi;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

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

    private void assertListEquals(List<String> plugins, String... names) {
        if (plugins.size() != names.length) {
            fail("Sizes differs.\nResolved: " + plugins + "\nExpected: " + Arrays.deepToString(names));
        }
        for (int i = 0; i < plugins.size() && i < names.length; i++) {
            assertEquals(plugins.get(i), names[i], "\nResolved: " + plugins + "\nExpected: " + Arrays.deepToString(names));
        }
    }

}
