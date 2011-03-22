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

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.EndsWith;

/**
 * Unit test of {@link PropertiesJettyRunWarConfig}.
 */
public class PropertiesJettyRunWarConfigTest {

    @Test
    public final void testDefaultValues()
    {
        final PropertiesJettyRunWarConfig config = new PropertiesJettyRunWarConfig();
        config.init(new Properties());

        Assert.assertEquals(9090, config.getServerPort());
        Assert.assertEquals("/", config.getContextPath());
        Assert.assertThat(config.getServerLifeCycleListener(), notNullValue());
        Assert.assertThat(config.getServerLifeCycleListener(), instanceOf(NopServerLifeCycleListener.class));
        Assert.assertTrue(config.isDoStartServer());
        Assert.assertTrue(config.isDoDeployWebapp());
        Assert.assertFalse(config.isSkip());

        try {
            config.getWarLocation();
            Assert.fail("must throw " + IllegalArgumentException.class);
        }
        catch (final IllegalArgumentException e) {
            assertThat(e.getCause(), instanceOf(FileNotFoundException.class));
        }
    }


    @Test
    public final void testGetWarLocation()
    {
        final PropertiesJettyRunWarConfig config = new PropertiesJettyRunWarConfig();
        config.init(this.properties);

        assertThat(config.getWarLocation().getPath(), new EndsWith(
                "/mycila-testing/mycila-testing-plugins/mycila-testing-jetty/pom.xml"));
    }


    @Test
    public final void testGetServerPort()
    {
        final PropertiesJettyRunWarConfig config = new PropertiesJettyRunWarConfig();
        config.init(this.properties);

        Assert.assertEquals(7894, config.getServerPort());
    }


    @Test
    public final void testGetContextPath()
    {
        final PropertiesJettyRunWarConfig config = new PropertiesJettyRunWarConfig();
        config.init(this.properties);

        Assert.assertEquals("/some/path", config.getContextPath());
    }


    @Test
    public final void testGetServerLifeCycleListener()
    {
        final PropertiesJettyRunWarConfig config = new PropertiesJettyRunWarConfig();
        config.init(this.properties);

        Assert.assertThat(config.getServerLifeCycleListener(), instanceOf(OtherListener.class));
    }


    @Test
    public final void testIsDoStartServer()
    {
        final PropertiesJettyRunWarConfig config = new PropertiesJettyRunWarConfig();
        config.init(this.properties);

        Assert.assertFalse(config.isDoStartServer());
    }


    @Test
    public final void testIsDoDeployWebapp()
    {
        final PropertiesJettyRunWarConfig config = new PropertiesJettyRunWarConfig();
        config.init(this.properties);

        Assert.assertFalse(config.isDoDeployWebapp());
    }


    @Test
    public final void testIsSkip()
    {
        final PropertiesJettyRunWarConfig config = new PropertiesJettyRunWarConfig();
        config.init(this.properties);

        Assert.assertTrue(config.isSkip());
    }


    @Before
    public void initProperties()
        throws IOException
    {
        this.properties = new Properties();
        final String resource = "/" + this.getClass().getSimpleName() + ".properties";
        this.properties.load(this.getClass().getResourceAsStream(resource));
    }

    private Properties properties;

    public static class OtherListener
            extends NopServerLifeCycleListener {
        // nop
    }

}
