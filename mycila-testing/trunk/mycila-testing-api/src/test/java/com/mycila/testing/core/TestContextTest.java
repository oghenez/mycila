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
import com.mycila.testing.core.annot.MycilaPlugins;
import static com.mycila.testing.core.api.Cache.*;
import com.mycila.testing.core.api.TestPluginException;
import com.mycila.testing.core.plugin.TestPlugin;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@MycilaPlugins(UNSHARED)
public final class TestContextTest {

    PluginManager<TestPlugin> plugins;
    TestContextImpl test;

    @BeforeMethod
    public void setup() {
        plugins = new PluginManager<TestPlugin>(TestPlugin.class);
        plugins.getCache().registerPlugin("myPlugin", new MyPlugin());
        test = new TestContextImpl(plugins, this);
        MyPlugin.prepared = false;
    }

    @Test
    public void test_attributes() throws Exception {
        assertFalse(test.attributes().containsKey("yo"));
        assertFalse(test.hasAttribute("yo"));
        test.setAttribute("yo", "man");
        assertTrue(test.hasAttribute("yo"));
        assertTrue(test.attributes().containsKey("yo"));
        assertEquals(test.attribute("yo"), "man");
        test.removeAttribute("yo");
        assertFalse(test.hasAttribute("yo"));
    }

    @Test(expectedExceptions = TestPluginException.class)
    public void test_attributes_inexisting() throws Exception {
        test.attribute("inexisting");
    }

    @Test
    public void test_instance() throws Exception {
        assertEquals(test.introspector().instance(), this);
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

    @Test
    public void test_fireEvents() throws Exception {
        assertFalse(MyPlugin.befores.contains("test_fireEvents"));
        assertFalse(MyPlugin.afters.contains("test_fireEvents"));
        assertFalse(MyPlugin.ends.contains("com.mycila.testing.core.TestContextTest"));

        test.fireBeforeTest(getClass().getMethod("test_fireEvents"));
        assertTrue(MyPlugin.befores.contains("test_fireEvents"));
        assertFalse(MyPlugin.afters.contains("test_fireEvents"));
        assertFalse(MyPlugin.ends.contains("com.mycila.testing.core.TestContextTest"));

        test.fireAfterTest();
        assertTrue(MyPlugin.befores.contains("test_fireEvents"));
        assertTrue(MyPlugin.afters.contains("test_fireEvents"));
        assertFalse(MyPlugin.ends.contains("com.mycila.testing.core.TestContextTest"));

        test.fireAfterClass();
        assertTrue(MyPlugin.befores.contains("test_fireEvents"));
        assertTrue(MyPlugin.afters.contains("test_fireEvents"));
        assertTrue(MyPlugin.ends.contains("com.mycila.testing.core.TestContextTest"));
    }

    @Test
    public void test_fireEvents_Exceptions1() throws Exception {
        try {
            test.fireBeforeTest(getClass().getMethod("test_fireEvents_Exceptions1"));
            fail();
        } catch (TestPluginException e) {
            e.printStackTrace();
            assertEquals(e.getCause().getMessage(), "Hello 1");
        }

        test.fireBeforeTest(getClass().getMethod("test_fireEvents_Exceptions2"));
        try {
            test.fireAfterTest();
            fail();
        } catch (TestPluginException e) {
            e.printStackTrace();
            assertEquals(e.getCause().getMessage(), "Hello 2");
        }
    }

    public void test_fireEvents_Exceptions2() {
    }
}
