package com.mycila.testing.core;

import com.mycila.plugin.spi.PluginManager;
import com.mycila.testing.FailingPlugin;
import com.mycila.testing.MyPlugin;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class TestContextTest {

    PluginManager<TestPlugin> plugins;
    TestContext test;

    @BeforeMethod
    public void setup() {
        plugins = new PluginManager<TestPlugin>(TestPlugin.class);
        plugins.getCache().registerPlugin("myPlugin", new MyPlugin());
        test = new TestContext(plugins, this);
        MyPlugin.executed = false;
    }

    @Test
    public void test_resolver() throws Exception {
        assertEquals(test.getPluginResolver(), plugins.getResolver());
    }

    @Test
    public void test_attributes() throws Exception {
        assertFalse(test.getAttributes().containsKey("yo"));
        assertFalse(test.hasAttribute("yo"));
        test.setAttribute("yo", "man");
        assertTrue(test.hasAttribute("yo"));
        assertTrue(test.getAttributes().containsKey("yo"));
        assertEquals(test.getAttribute("yo"), "man");
        test.removeAttribute("yo");
        assertFalse(test.hasAttribute("yo"));
    }

    @Test(expectedExceptions = TestPluginException.class)
    public void test_attributes_inexisting() throws Exception {
        test.getAttribute("inexisting");
    }

    @Test
    public void test_instance() throws Exception {
        assertEquals(test.getTestInstance(), this);
    }

    @Test
    public void test_execute() throws Exception {
        assertFalse(MyPlugin.executed);
        test.execute();
        assertTrue(MyPlugin.executed);
    }

    @Test(expectedExceptions = TestPluginException.class)
    public void test_execute_exception() throws Exception {
        plugins.getCache().registerPlugin("failing", new FailingPlugin());
        test.execute();
    }
}
