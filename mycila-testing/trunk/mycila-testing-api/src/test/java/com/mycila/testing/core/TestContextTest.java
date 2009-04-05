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
        MyPlugin.prepared = false;
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
        assertEquals(test.getTest().getTarget(), this);
    }

    @Test(expectedExceptions = TestPluginException.class)
    public void test_execute_exception() throws Exception {
        plugins.getCache().registerPlugin("failing", new FailingPlugin());
        test.prepare();
    }

    @Test
    public void test_execute() throws Exception {
        assertFalse(MyPlugin.prepared);
        test.prepare();
        assertTrue(MyPlugin.prepared);
    }
    
}
