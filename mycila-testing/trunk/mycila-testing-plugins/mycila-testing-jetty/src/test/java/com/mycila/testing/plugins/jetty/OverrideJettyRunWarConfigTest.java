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
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.matchers.EndsWith;

/**
 * Unit test of {@link OverrideJettyRunWarConfig}.
 */
public class OverrideJettyRunWarConfigTest {

    @Test
    public final void testGetWarLocation()
    {
        @JettyRunWar
        class Dummy {
            // nop
        }
        final JettyRunWar jettyRunWar = Dummy.class.getAnnotation(JettyRunWar.class);
        final OverrideJettyRunWarConfig config = new OverrideJettyRunWarConfig(this.defaultConfig);
        config.init(jettyRunWar);

        Assert.assertEquals(5345, config.getServerPort());
        Assert.assertEquals("/", config.getContextPath());
        Assert.assertThat(config.getServerLifeCycleListener(), notNullValue());
        Assert.assertThat(config.getServerLifeCycleListener(), instanceOf(ServerLifeCycleListener.class));
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
    public final void testGetWarLocationFromValue()
    {
        @JettyRunWar("pom.xml")
        class Dummy {
            // nop
        }
        final JettyRunWar jettyRunWar = Dummy.class.getAnnotation(JettyRunWar.class);
        final OverrideJettyRunWarConfig config = new OverrideJettyRunWarConfig(this.defaultConfig);
        config.init(jettyRunWar);

        assertThat(config.getWarLocation().getPath(), new EndsWith(
                "/mycila-testing/mycila-testing-plugins/mycila-testing-jetty/pom.xml"));
    }


    @Test
    public final void testGetWarLocationFromWar()
    {
        @JettyRunWar(war = "pom.xml")
        class Dummy {
            // nop
        }
        final JettyRunWar jettyRunWar = Dummy.class.getAnnotation(JettyRunWar.class);
        final OverrideJettyRunWarConfig config = new OverrideJettyRunWarConfig(this.defaultConfig);
        config.init(jettyRunWar);

        assertThat(config.getWarLocation().getPath(), new EndsWith(
                "/mycila-testing/mycila-testing-plugins/mycila-testing-jetty/pom.xml"));
    }


    @Test
    public final void testGetServerPort()
    {
        @JettyRunWar(serverPort = 4567)
        class Dummy {
            // nop
        }
        final JettyRunWar jettyRunWar = Dummy.class.getAnnotation(JettyRunWar.class);
        final OverrideJettyRunWarConfig config = new OverrideJettyRunWarConfig(this.defaultConfig);
        config.init(jettyRunWar);

        Assert.assertEquals(4567, config.getServerPort());
    }


    @Test
    public final void testGetContextPath()
    {
        @JettyRunWar(contextPath = "/source/to")
        class Dummy {
            // nop
        }
        final JettyRunWar jettyRunWar = Dummy.class.getAnnotation(JettyRunWar.class);
        final OverrideJettyRunWarConfig config = new OverrideJettyRunWarConfig(this.defaultConfig);
        config.init(jettyRunWar);

        Assert.assertEquals("/source/to", config.getContextPath());
    }


    @Test
    public final void testGetServerLifeCycleListener()
    {
        @JettyRunWar(serverLifeCycleListener = AnotherListener.class)
        class Dummy {
            // nop
        }
        final JettyRunWar jettyRunWar = Dummy.class.getAnnotation(JettyRunWar.class);
        final OverrideJettyRunWarConfig config = new OverrideJettyRunWarConfig(this.defaultConfig);
        config.init(jettyRunWar);

        Assert.assertThat(config.getServerLifeCycleListener(), instanceOf(AnotherListener.class));
    }

    public static class AnotherListener
            extends NopServerLifeCycleListener {
        // nop
    }


    @Test
    public final void testIsDoStartServer()
    {
        @JettyRunWar(startServer = false)
        class Dummy {
            // nop
        }
        final JettyRunWar jettyRunWar = Dummy.class.getAnnotation(JettyRunWar.class);
        final OverrideJettyRunWarConfig config = new OverrideJettyRunWarConfig(this.defaultConfig);
        config.init(jettyRunWar);

        Assert.assertFalse(config.isDoStartServer());
    }


    @Test
    public final void testIsDoDeployWebapp()
    {
        @JettyRunWar(deployWebapp = false)
        class Dummy {
            // nop
        }
        final JettyRunWar jettyRunWar = Dummy.class.getAnnotation(JettyRunWar.class);
        final OverrideJettyRunWarConfig config = new OverrideJettyRunWarConfig(this.defaultConfig);
        config.init(jettyRunWar);

        Assert.assertFalse(config.isDoDeployWebapp());
    }


    @Test
    public final void testIsSkip()
    {
        @JettyRunWar(skip = true)
        class Dummy {
            // nop
        }
        final JettyRunWar jettyRunWar = Dummy.class.getAnnotation(JettyRunWar.class);
        final OverrideJettyRunWarConfig config = new OverrideJettyRunWarConfig(this.defaultConfig);
        config.init(jettyRunWar);

        Assert.assertTrue(config.isSkip());
    }


    @Before
    public void init()
    {
        this.defaultConfig = new DefaultJettyRunWarConfig() {

            @Override
            public URL getWarLocation()
            {
                try {
                    return new URL("file://some/file");
                }
                catch (final MalformedURLException e) {
                    throw new AssertionError(e);
                }
            }


            @Override
            public int getServerPort()
            {
                return 5345;
            }


            @Override
            public String getContextPath()
            {
                return "/path/to";
            }


            @Override
            public ServerLifeCycleListener getServerLifeCycleListener()
            {
                return Mockito.mock(ServerLifeCycleListener.class);
            }


            @Override
            public boolean isDoStartServer()
            {
                return true;
            }


            @Override
            public boolean isDoDeployWebapp()
            {
                return true;
            }


            @Override
            public boolean isSkip()
            {
                return false;
            }

        };
    }

    private JettyRunWarConfig defaultConfig;

}
