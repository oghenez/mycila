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

package com.mycila.testing.plugins.jetty;

import static com.mycila.testing.plugins.jetty.AbstractDefaultJettyRunWarConfig.DEFAULT_CONTEXT_PATH;
import static com.mycila.testing.plugins.jetty.AbstractDefaultJettyRunWarConfig.DEFAULT_SERVER_PORT;
import static com.mycila.testing.plugins.jetty.AbstractDefaultJettyRunWarConfig.DEFAULT_WAR_LOCATION;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit test of {@link JettyRunWarHelper}.
 */
public class JettyRunWarHelperTest {
    
    @Test
    public void testDefaultValues()
    {
        assertEquals(DEFAULT_WAR_LOCATION, JettyRunWarHelper.getDefaultValue());
        assertEquals(DEFAULT_CONTEXT_PATH, JettyRunWarHelper.getDefaultContextPath());
        assertEquals(DEFAULT_SERVER_PORT, JettyRunWarHelper.getDefaultServerPort());
        
        assertEquals("http://localhost:" + DEFAULT_SERVER_PORT, JettyRunWarHelper.getDefaultWebappUrl());
    }
    

    @Test
    public void testGetWebappUrl()
    {
        assertEquals("http://localhost:8888/path/to", JettyRunWarHelper.getWebappUrl(WebappTest.class));
        assertEquals("http://localhost:1234/context", JettyRunWarHelper.getWebappUrl(AnotherWebappTest.class));
    }
    

    @JettyRunWar(serverPort = 8888, contextPath = "/path/to")
    static class WebappTest {
        // nop
    }
    
    @JettyRunWar(config = AnotherConfig.class)
    static class AnotherWebappTest {
        // nop
    }
    
    static class AnotherConfig
            extends DefaultJettyRunWarConfig {
        
        @Override
        public int getServerPort()
        {
            return 1234;
        }
        

        @Override
        public String getContextPath()
        {
            return "/context";
        }
        
    }
    
}
