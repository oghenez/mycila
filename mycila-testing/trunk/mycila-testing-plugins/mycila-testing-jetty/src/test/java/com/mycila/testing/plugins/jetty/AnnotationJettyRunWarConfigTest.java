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
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.matchers.EndsWith;

/**
 * Unit test of {@link AnnotationJettyRunWarConfig}.
 */
public class AnnotationJettyRunWarConfigTest {

    @Test
    public final void testDefaultValues()
        throws Exception
    {
        @JettyRunWar
        class Dummy {
            // nop
        }
        final JettyRunWar jettyRunWar = Dummy.class.getAnnotation(JettyRunWar.class);
        final AnnotationJettyRunWarConfig config = new AnnotationJettyRunWarConfig();
        config.init(jettyRunWar);

        Assert.assertEquals(9090, config.getServerPort());
        Assert.assertEquals("/", config.getContextPath());
        Assert.assertThat(config.getServerLifeCycleListener(), notNullValue());
        Assert.assertThat(config.getServerLifeCycleListener(), instanceOf(NopServerLifeCycleListener.class));
        // TODO Assert.assertTrue(config.isDoStartServer());
        // TODO Assert.assertTrue(config.isDoDeployWebapp());
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
        throws Exception
    {
        @JettyRunWar("pom.xml")
        class Dummy {
            // nop
        }
        final JettyRunWar jettyRunWar = Dummy.class.getAnnotation(JettyRunWar.class);
        final AnnotationJettyRunWarConfig config = new AnnotationJettyRunWarConfig();
        config.init(jettyRunWar);

        final URL url = config.getWarLocation();
        assertThat(url.getPath(), new EndsWith("/mycila-testing/mycila-testing-plugins/mycila-testing-jetty/pom.xml"));
    }


    @Test
    public final void testGetServerPort()
    {
        @JettyRunWar(serverPort = 1324)
        class Dummy {
            // nop
        }
        final JettyRunWar jettyRunWar = Dummy.class.getAnnotation(JettyRunWar.class);
        final AnnotationJettyRunWarConfig config = new AnnotationJettyRunWarConfig();
        config.init(jettyRunWar);

        Assert.assertEquals(1324, config.getServerPort());
    }


    @Test
    public final void testGetContextPath()
    {
        @JettyRunWar(contextPath = "/path/to")
        class Dummy {
            // nop
        }
        final JettyRunWar jettyRunWar = Dummy.class.getAnnotation(JettyRunWar.class);
        final AnnotationJettyRunWarConfig config = new AnnotationJettyRunWarConfig();
        config.init(jettyRunWar);

        Assert.assertEquals("/path/to", config.getContextPath());
    }


    @Test
    public final void testGetServerLifeCycleListener()
    {
        @JettyRunWar(serverLifeCycleListener = SomeListener.class)
        class Dummy {
            // nop
        }
        final JettyRunWar jettyRunWar = Dummy.class.getAnnotation(JettyRunWar.class);
        final AnnotationJettyRunWarConfig config = new AnnotationJettyRunWarConfig();
        config.init(jettyRunWar);

        Assert.assertThat(config.getServerLifeCycleListener(), instanceOf(SomeListener.class));
    }


    @Test
    public final void testIsDoStartServer()
    {
        // TODO testIsDoStartServer
    }


    @Test
    public final void testIsDoDeployWebapp()
    {
        // TODO testIsDoDeployWebapp
    }


    @Test
    public final void testIsSkip()
    {
        @JettyRunWar(skip = true)
        class Dummy {
            // nop
        }
        final JettyRunWar jettyRunWar = Dummy.class.getAnnotation(JettyRunWar.class);
        final AnnotationJettyRunWarConfig config = new AnnotationJettyRunWarConfig();
        config.init(jettyRunWar);

        Assert.assertTrue(config.isSkip());
    }

    public static class SomeListener
            extends NopServerLifeCycleListener {
        // nop
    }

}
