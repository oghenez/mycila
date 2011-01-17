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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.mycila.testing.junit.MycilaJunitRunner;
import com.mycila.testing.plugins.jetty.JettyRunWar;

import static org.junit.Assert.assertEquals;

/**
 * Test that the WAR is found and loaded.
 */
@RunWith(MycilaJunitRunner.class)
@JettyRunWar(targetWebapp = JettyRunWar.TargetWebapp.ALREADY_RUNNING)
public class TargetWebappAlreadyRunningJettyRunWarIT {
    
    /**
     * Test that HelloWorld page is accessible.
     */
    @Test
    public void testHelloWorld()
    {
        this.webDriver.get(this.getPage("hi.jsp"));
        assertEquals("about:blank", this.webDriver.getCurrentUrl());
    }
    

    @Before
    public final void initWebDriver()
    {
        final HtmlUnitDriver unitDriver = new HtmlUnitDriver();
        
        this.webDriver = unitDriver;
    }
    

    @After
    public final void quitWebDriver()
    {
        this.webDriver.quit();
    }
    

    protected String getPage(
            final String page)
    {
        final String path = this.server + ":" + this.port + this.contextPath + page;
        return path;
    }
    

    private WebDriver webDriver;
    
    private final String server = "http://localhost";
    
    private final int port = 9090;
    
    private final String contextPath = "/";
    
}
