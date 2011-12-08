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

package com.mycila.testing.plugins.jetty.config;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.text.StringEndsWith.endsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import com.mycila.testing.plugins.jetty.JettyRunWar;
import com.mycila.testing.plugins.jetty.NopServerLifeCycleListener;
import com.mycila.testing.plugins.jetty.ServerLifeCycleListener;

/**
 * Unit test of {@link DefaultConfig}.
 */
public class DefaultConfigTest {

    public static class DefaultConstructor {
    }

    public static class EmptyConstructor {
        public EmptyConstructor() {}
    }

    public static class OneArgConstructor {
        public OneArgConstructor(Map map) {}
    }

    public static class EmptyAndOneArgConstructor {
        public EmptyAndOneArgConstructor() {}
        public EmptyAndOneArgConstructor(Map map) {}
    }

    @Test
    public void testConstructors()
            throws NoSuchMethodException
    {
        assertEquals(1, DefaultConstructor.class.getConstructors().length);
        DefaultConstructor.class.getConstructor();
        assertEquals(1, DefaultConstructor.class.getDeclaredConstructors().length);
        DefaultConstructor.class.getDeclaredConstructors();

        assertEquals(1, EmptyConstructor.class.getConstructors().length);
        EmptyConstructor.class.getConstructor();
        assertEquals(1, EmptyConstructor.class.getDeclaredConstructors().length);
        EmptyConstructor.class.getDeclaredConstructors();

        assertEquals(1, OneArgConstructor.class.getConstructors().length);
        OneArgConstructor.class.getConstructor(Map.class);
        assertEquals(1, OneArgConstructor.class.getDeclaredConstructors().length);
        OneArgConstructor.class.getDeclaredConstructor(Map.class);

        assertEquals(2, EmptyAndOneArgConstructor.class.getConstructors().length);
        EmptyAndOneArgConstructor.class.getConstructor();
        EmptyAndOneArgConstructor.class.getConstructor(Map.class);
        assertEquals(2, EmptyAndOneArgConstructor.class.getDeclaredConstructors().length);
        EmptyAndOneArgConstructor.class.getDeclaredConstructors();
        EmptyAndOneArgConstructor.class.getDeclaredConstructor(Map.class);
    }

    static class NoConstructorConfig extends DefaultConfig {
        public Class<? extends ServerLifeCycleListener> getServerLifeCycleListenerClass()
        {
            return NoConstructorContextListener.class;
        }
    }
    static class NoConstructorContextListener
            extends NopServerLifeCycleListener {
    }

    @Test
    public void testNoConstructorContextListener() {
        final NoConstructorConfig config = new NoConstructorConfig();
        assertThat(config.getServerLifeCycleListener(null), instanceOf(NopServerLifeCycleListener.class));
    }

    static class DefaultConstructorConfig extends DefaultConfig {
        public Class<? extends ServerLifeCycleListener> getServerLifeCycleListenerClass()
        {
            return DefaultConstructorContextListener.class;
        }
    }
    static class DefaultConstructorContextListener
            extends NopServerLifeCycleListener {
        public DefaultConstructorContextListener() {}
    }

    @Test
    public void testDefaultConstructorContextListener() {
        final DefaultConstructorConfig config = new DefaultConstructorConfig();
        assertThat(config.getServerLifeCycleListener(null), instanceOf(NopServerLifeCycleListener.class));
    }

    static class OneArgConstructorConfig extends DefaultConfig {
        public Class<? extends ServerLifeCycleListener> getServerLifeCycleListenerClass()
        {
            return OneArgConstructorContextListener.class;
        }
    }
    static class OneArgConstructorContextListener
            extends NopServerLifeCycleListener {
        public OneArgConstructorContextListener(Map context) {}
    }

    @Test
    public void testOneArgConstructorContextListener() {
        final OneArgConstructorConfig config = new OneArgConstructorConfig();
        assertThat(config.getServerLifeCycleListener(null), instanceOf(NopServerLifeCycleListener.class));
    }

    static class DefaultAndOneArgConstructorConfig extends DefaultConfig {
        public Class<? extends ServerLifeCycleListener> getServerLifeCycleListenerClass()
        {
            return DefaultAndOneArgConstructorContextListener.class;
        }
    }
    static class DefaultAndOneArgConstructorContextListener
            extends NopServerLifeCycleListener {
        public DefaultAndOneArgConstructorContextListener() {}
        public DefaultAndOneArgConstructorContextListener(Map context) {}
    }

    @Test
    public void testDefaultAndOneArgConstructorContextListener() {
        final DefaultAndOneArgConstructorConfig config = new DefaultAndOneArgConstructorConfig();
        assertThat(config.getServerLifeCycleListener(null), instanceOf(NopServerLifeCycleListener.class));
    }


    @Test
    public final void testRawConfig()
    {
        final RawConfig rawConfig = new RawConfig() {

            public boolean isStartServer()
            {
                return false;
            }


            public boolean isSkip()
            {
                return true;
            }


            public boolean isDeployWebapp()
            {
                return false;
            }


            public String getWarLocation()
            {
                return "WarLocation";
            }


            public int getServerPort()
            {
                return 1;
            }


            public Class<? extends ServerLifeCycleListener> getServerLifeCycleListenerClass()
            {
                return null;
            }


            public String getContextPath()
            {
                return "ContextPath";
            }
        };

        final DefaultConfig config = new DefaultConfig(rawConfig);
        assertEquals("WarLocation", config.getWarLocation());
        assertEquals("ContextPath", config.getContextPath());
        assertEquals(1, config.getServerPort());
        assertFalse(config.isStartServer());
        assertFalse(config.isDeployWebapp());
        assertTrue(config.isSkip());
        assertNull(config.getServerLifeCycleListenerClass());
    }


    @Test
    public final void testConfig()
    {
        final RawConfig rawConfig = new RawConfig() {

            public boolean isStartServer()
            {
                return false;
            }


            public boolean isSkip()
            {
                return true;
            }


            public boolean isDeployWebapp()
            {
                return false;
            }


            public String getWarLocation()
            {
                return "pom.xml";
            }


            public int getServerPort()
            {
                return 1;
            }


            public Class<? extends ServerLifeCycleListener> getServerLifeCycleListenerClass()
            {
                return NopServerLifeCycleListener.class;
            }


            public String getContextPath()
            {
                return "ContextPath";
            }
        };

        final DefaultConfig config = new DefaultConfig(rawConfig);
        assertThat(config.getWarLocationUrl().toString(), endsWith("/pom.xml"));
        assertThat(config.getServerLifeCycleListener(null), instanceOf(NopServerLifeCycleListener.class));
    }


    @Test
    public final void testConfigFromProperties()
        throws IOException
    {
        final Properties properties = new Properties();
        properties.load(this.getClass().getResourceAsStream("/test-config.properties"));

        final DefaultConfig config = new DefaultConfig(properties);
        assertEquals("pom.xml", config.getWarLocation());
        assertEquals("/some/path", config.getContextPath());
        assertEquals(7894, config.getServerPort());
        assertTrue(config.isStartServer());
        assertTrue(config.isDeployWebapp());
        assertTrue(config.isSkip());
        assertEquals(ClassListener.class, config.getServerLifeCycleListenerClass());
    }


    @Test
    public final void testConfigFromPropertiesEmpty()
    {
        final Properties properties = new Properties();

        final DefaultConfig config = new DefaultConfig(properties);
        assertEquals("reg:.*\\.war", config.getWarLocation());
        assertEquals("/its", config.getContextPath());
        assertEquals(9090, config.getServerPort());
        assertTrue(config.isStartServer());
        assertTrue(config.isDeployWebapp());
        assertFalse(config.isSkip());
        assertEquals(NopServerLifeCycleListener.class, config.getServerLifeCycleListenerClass());
    }


    @Test
    public final void testDefaultValues()
    {
        final DefaultConfig config = new DefaultConfig();
        assertEquals("reg:.*\\.war", config.getWarLocation());
        assertEquals("/its", config.getContextPath());
        assertEquals(9090, config.getServerPort());
        assertTrue(config.isStartServer());
        assertTrue(config.isDeployWebapp());
        assertFalse(config.isSkip());
        assertEquals(NopServerLifeCycleListener.class, config.getServerLifeCycleListenerClass());
    }


    @Test
    public final void testIsDefaultFromDefault()
    {
        final DefaultConfig config = new DefaultConfig();
        assertTrue(config.isDefaultWarLocation());
        assertTrue(config.isDefaultContextPath());
        assertTrue(config.isDefaultServerPort());
        assertTrue(config.isDefaultStartServer());
        assertTrue(config.isDefaultDeployWebapp());
        assertTrue(config.isDefaultSkip());
        assertTrue(config.isDefaultServerLifeCycleListenerClass());
    }


    @Test
    public final void testIsDefaultFromSet()
    {
        final DefaultConfig config = new DefaultConfig();
        config.setWarLocation("WarLocation");
        assertFalse(config.isDefaultWarLocation());
        config.setContextPath("ContextPath");
        assertFalse(config.isDefaultContextPath());
        config.setServerPort(1);
        assertFalse(config.isDefaultServerPort());
        config.setStartServer(false);
        assertFalse(config.isDefaultStartServer());
        config.setDeployWebapp(false);
        assertFalse(config.isDefaultDeployWebapp());
        config.setSkip(true);
        assertFalse(config.isDefaultSkip());
        config.setServerLifeCycleListenerClass(null);
        assertFalse(config.isDefaultServerLifeCycleListenerClass());
    }


    @Test
    public final void testThatThrowsIllegalArgumentExceptionIfNoAnnotationAtAll()
        throws Exception
    {
        class Dummy {
            public void test()
            {
                // nop
            }
        }

        final Method method = Dummy.class.getMethod("test");
        assertFalse(DefaultConfig.hasJettyPlugin(method));

        try {
            DefaultConfig.configFrom(method);
            Assert.fail("must throws " + IllegalArgumentException.class);
        }
        catch (final IllegalArgumentException e) {
            assertEquals(
                    "at least " + method.getDeclaringClass() + " should be annotated with " + JettyRunWar.class,
                    e.getMessage());
        }
    }


    @Test
    public final void testRawConfigOfClassAnnotationWithDefaultConfig()
        throws Exception
    {
        @JettyRunWar
        class Dummy {
            public void test()
            {
                // nop
            }
        }

        final Method method = Dummy.class.getMethod("test");
        assertTrue(DefaultConfig.hasJettyPlugin(method));

        final RawConfig config = DefaultConfig.configFrom(method);
        assertEquals("reg:.*\\.war", config.getWarLocation());
        assertEquals(9090, config.getServerPort());
        assertEquals("/its", config.getContextPath());
        assertTrue(config.isStartServer());
        assertTrue(config.isDeployWebapp());
        assertFalse(config.isSkip());
        assertEquals(NopServerLifeCycleListener.class, config.getServerLifeCycleListenerClass());
    }


    @Test
    public final void testRawConfigOfClassAnnotationWithClassConfig()
        throws Exception
    {
        @JettyRunWar(ClassConfig.class)
        class Dummy {
            public void test()
            {
                // nop
            }
        }

        final Method method = Dummy.class.getMethod("test");
        assertTrue(DefaultConfig.hasJettyPlugin(method));

        final RawConfig config = DefaultConfig.configFrom(method);
        assertEquals("class", config.getWarLocation());
        assertEquals(1234, config.getServerPort());
        assertEquals("/class", config.getContextPath());
        assertTrue(config.isStartServer());
        assertTrue(config.isDeployWebapp());
        assertFalse(config.isSkip());
        assertEquals(ClassListener.class, config.getServerLifeCycleListenerClass());
    }


    @Test
    public final void testRawConfigOfMethodAnnotationMustHaveClassAnnotation()
        throws Exception
    {
        class Dummy {
            @JettyRunWar
            public void test()
            {
                // nop
            }
        }

        final Method method = Dummy.class.getMethod("test");
        assertFalse(DefaultConfig.hasJettyPlugin(method));

        try {
            DefaultConfig.configFrom(method);
            Assert.fail("must throws " + IllegalArgumentException.class);
        }
        catch (final IllegalArgumentException e) {
            assertEquals(
                    "at least " + method.getDeclaringClass() + " should be annotated with " + JettyRunWar.class,
                    e.getMessage());
        }
    }


    @Test
    public final void testRawConfigOfMethodAnnotationWithClassDefaultConfigAndMethodDefaultConfig()
        throws Exception
    {
        @JettyRunWar
        class Dummy {
            @JettyRunWar
            public void test()
            {
                // nop
            }
        }

        final Method method = Dummy.class.getMethod("test");
        assertTrue(DefaultConfig.hasJettyPlugin(method));

        try {
            DefaultConfig.configFrom(method);
            Assert.fail("must throws " + IllegalArgumentException.class);
        }
        catch (final IllegalArgumentException e) {
            assertEquals(method + " should not be annotated with " + JettyRunWar.class
                    + " because it has the same configuration has its class " + Dummy.class, e.getMessage());
        }
    }


    @Test
    public final void testRawConfigOfMethodAnnotationWithClassDefaultConfigAndMethodConfig()
        throws Exception
    {
        @JettyRunWar
        class Dummy {
            @JettyRunWar(MethodConfig.class)
            public void test()
            {
                // nop
            }
        }

        final Method method = Dummy.class.getMethod("test");
        assertTrue(DefaultConfig.hasJettyPlugin(method));

        final RawConfig config = DefaultConfig.configFrom(method);
        assertEquals("method", config.getWarLocation());
        assertEquals(9876, config.getServerPort());
        assertEquals("/method", config.getContextPath());
        assertFalse(config.isStartServer());
        assertFalse(config.isDeployWebapp());
        assertTrue(config.isSkip());
        assertEquals(MethodListener.class, config.getServerLifeCycleListenerClass());
    }


    @Test
    public final void testRawConfigOfMethodAnnotationWithClassConfigAndMethodConfig()
        throws Exception
    {
        @JettyRunWar(ClassConfig.class)
        class Dummy {
            @JettyRunWar(MethodConfig.class)
            public void test()
            {
                // nop
            }
        }

        final Method method = Dummy.class.getMethod("test");
        assertTrue(DefaultConfig.hasJettyPlugin(method));

        final RawConfig config = DefaultConfig.configFrom(method);
        assertEquals("method", config.getWarLocation());
        assertEquals(9876, config.getServerPort());
        assertEquals("/method", config.getContextPath());
        assertFalse(config.isStartServer());
        assertFalse(config.isDeployWebapp());
        assertTrue(config.isSkip());
        assertEquals(MethodListener.class, config.getServerLifeCycleListenerClass());
    }


    @Test
    public final void testRawConfigOfMethodAnnotationWithClassConfigAndMethodDefaultConfig()
        throws Exception
    {
        @JettyRunWar(ClassConfig.class)
        class Dummy {
            @JettyRunWar
            public void test()
            {
                // nop
            }
        }

        final Method method = Dummy.class.getMethod("test");
        assertTrue(DefaultConfig.hasJettyPlugin(method));

        try {
            DefaultConfig.configFrom(method);
            Assert.fail("must throws " + IllegalArgumentException.class);
        }
        catch (final IllegalArgumentException e) {
            assertEquals(method + " must be annotated with " + JettyRunWar.class
                    + " and has a custom configuration because its class " + Dummy.class
                    + " has a custom configuration", e.getMessage());
        }
    }

    static class ClassListener
            extends NopServerLifeCycleListener {
        // nop
    }

    static class MethodListener
            extends NopServerLifeCycleListener {
        // nop
    }

    static class ClassConfig
            implements RawConfig {

        public String getWarLocation()
        {
            return "class";
        }


        public int getServerPort()
        {
            return 1234;
        }


        public String getContextPath()
        {
            return "/class";
        }


        public boolean isStartServer()
        {
            return true;
        }


        public boolean isDeployWebapp()
        {
            return true;
        }


        public boolean isSkip()
        {
            return false;
        }


        public Class<? extends ServerLifeCycleListener> getServerLifeCycleListenerClass()
        {
            return ClassListener.class;
        }

    }

    static class MethodConfig
            implements RawConfig {

        public String getWarLocation()
        {
            return "method";
        }


        public int getServerPort()
        {
            return 9876;
        }


        public String getContextPath()
        {
            return "/method";
        }


        public boolean isStartServer()
        {
            return false;
        }


        public boolean isDeployWebapp()
        {
            return false;
        }


        public boolean isSkip()
        {
            return true;
        }


        public Class<? extends ServerLifeCycleListener> getServerLifeCycleListenerClass()
        {
            return MethodListener.class;
        }

    }

}
