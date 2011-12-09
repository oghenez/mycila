/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.test;

import com.mycila.testing.junit.MycilaJunitRunner;
import com.mycila.testing.plugins.jetty.JettyRunWar;
import com.mycila.testing.plugins.jetty.NopServerLifeCycleListener;
import com.mycila.testing.plugins.jetty.ServerLifeCycleListener;
import com.mycila.testing.plugins.jetty.config.DefaultConfig;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.Map;

import static com.mycila.testing.plugins.jetty.WebappHelper.getWebappUrl;
import static org.junit.Assert.*;

/**
 * Test that the WAR is found and loaded.
 */
@RunWith(MycilaJunitRunner.class)
@JettyRunWar(ListenerScopeIT.DummyConfig.class)
public class ListenerScopeIT {

    /**
     * Test that HelloWorld page is accessible.
     */
    @Test
    public void testHelloWorld1() {
        this.webDriver.get(getWebappUrl(this) + "/hi.jsp");

        assertEquals("Hello World !", this.webDriver.getTitle());
        assertTrue(DummyServerLifeCycleListener.hasKey);
        DummyServerLifeCycleListener.hasKey = false;
    }


    /**
     * Test that HelloWorld page is accessible.
     */
    @Test
    public void testHelloWorld2() {
        this.webDriver.get(getWebappUrl(this) + "/hi.jsp");

        assertEquals("Hello World !", this.webDriver.getTitle());
        assertTrue(DummyServerLifeCycleListener.hasKey);
        DummyServerLifeCycleListener.hasKey = false;
    }

    @Before
    public void initWebDriver() {
        final HtmlUnitDriver unitDriver = new HtmlUnitDriver();

        this.webDriver = unitDriver;
    }


    @After
    public final void quitWebDriver() {
        this.webDriver.quit();
    }

    private WebDriver webDriver;

    public static class DummyConfig extends DefaultConfig {
        @Override
        public Class<? extends ServerLifeCycleListener> getServerLifeCycleListenerClass() {
            return DummyServerLifeCycleListener.class;
        }
    }

    public static class DummyServerLifeCycleListener extends NopServerLifeCycleListener {
        public DummyServerLifeCycleListener() {
        }
        public DummyServerLifeCycleListener(Map context) {
            if (!context.containsKey("key")) {
                context.put("key", "value");
            }
            else {
                assertEquals("value", context.get("key"));
            }

            hasKey = context.containsKey("key");
        }
        public static boolean hasKey;
    }

}
