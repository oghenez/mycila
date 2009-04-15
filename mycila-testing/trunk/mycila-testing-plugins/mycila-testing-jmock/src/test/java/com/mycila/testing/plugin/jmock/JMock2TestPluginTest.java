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
package com.mycila.testing.plugin.jmock;

import com.mycila.testing.core.api.TestContext;
import com.mycila.testing.core.introspect.Introspector;
import org.jmock.Expectations;
import org.jmock.Mockery;
import static org.testng.Assert.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class JMock2TestPluginTest {

    Mockery mockery;
    Mockery mockery3 = new Mockery();
    TestContext ctx;
    JMock2TestPlugin plugin = new JMock2TestPlugin();

    @BeforeMethod
    public void setup() {
        mockery = new Mockery();
        ctx = mockery.mock(TestContext.class);
    }

    @AfterMethod
    public void verify() {
        mockery.assertIsSatisfied();
    }

    @Test
    public void test_do_not_override_non_annotated_mockery() throws Exception {
        mockery.checking(new Expectations() {{
            allowing(ctx).introspector();
            will(returnValue(new Introspector(JMock2TestPluginTest.this)));
            one(ctx).setAttribute(with(JMock2TestPlugin.CTX_MOCKERY), with(any(Mockery.class)));
        }});
        Mockery current = mockery;
        plugin.prepareTestInstance(ctx);
        assertEquals(mockery, current);
    }

    @Test
    public void test_inject_mockery() throws Exception {
        final Test5 test = new Test5();
        mockery.checking(new Expectations() {{
            allowing(ctx).introspector();
            will(returnValue(new Introspector(test)));
            one(ctx).setAttribute(with(JMock2TestPlugin.CTX_MOCKERY), with(any(Mockery.class)));
        }});
        assertNotNull(test.mockery1);
        assertNull(test.mockery2);
        Mockery current = test.mockery1;
        plugin.prepareTestInstance(ctx);
        assertNotNull(test.mockery1);
        assertNotNull(test.mockery2);
        assertFalse(current.equals(test.mockery1));
    }

    @Test
    public void test_mock_interface() throws Exception {
        final Test1 test = new Test1();
        mockery.checking(new Expectations() {{
            allowing(ctx).introspector();
            will(returnValue(new Introspector(test)));
            one(ctx).setAttribute(with("org.jmock.Mockery"), with(any(Mockery.class)));
        }});
        assertNull(test.ctx);
        plugin.prepareTestInstance(ctx);
        assertNotNull(test.ctx);
    }

    @Test
    public void test_mock_class() throws Exception {
        final Test2 test = new Test2();
        mockery.checking(new Expectations() {{
            allowing(ctx).introspector();
            will(returnValue(new Introspector(test)));
            one(ctx).setAttribute(with("org.jmock.Mockery"), with(any(Mockery.class)));
        }});
        assertNull(test.ti);
        plugin.prepareTestInstance(ctx);
        assertNotNull(test.ti);
    }

    @Test
    public void test_enhance_superclasses() throws Exception {
        final Test4 test = new Test4();
        mockery.checking(new Expectations() {{
            allowing(ctx).introspector();
            will(returnValue(new Introspector(test)));
            one(ctx).setAttribute(with("org.jmock.Mockery"), with(any(Mockery.class)));
        }});
        assertNull(test.getTi3());
        assertNull(test.getTi4());
        plugin.prepareTestInstance(ctx);
        assertNotNull(test.getTi3());
        assertNotNull(test.getTi4());
    }

    @Test
    public void test_provider_field() throws Exception {
        final ProviderTest test = new ProviderTest() {
            @MockContextProvider
            private Mockery m = new Mockery();
        };
        mockery.checking(new Expectations() {{
            allowing(ctx).introspector();
            will(returnValue(new Introspector(test)));
            one(ctx).setAttribute(with("org.jmock.Mockery"), with(any(Mockery.class)));
        }});
        assertNull(test.get());
        plugin.prepareTestInstance(ctx);
        assertNotNull(test.get());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_provider_field_null() throws Exception {
        final ProviderTest test = new ProviderTest() {
            @MockContextProvider
            private Mockery m;
        };
        mockery.checking(new Expectations() {{
            allowing(ctx).introspector();
            will(returnValue(new Introspector(test)));
        }});
        assertNull(test.get());
        plugin.prepareTestInstance(ctx);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_provider_field_type_bad() throws Exception {
        final ProviderTest test = new ProviderTest() {
            @MockContextProvider
            private String m = "a string";
        };
        mockery.checking(new Expectations() {{
            allowing(ctx).introspector();
            will(returnValue(new Introspector(test)));
        }});
        assertNull(test.get());
        plugin.prepareTestInstance(ctx);
    }

    @Test
    public void test_provider_method() throws Exception {
        final ProviderTest test = new ProviderTest() {
            @MockContextProvider
            private Mockery build() {
                return new Mockery();
            }
        };
        mockery.checking(new Expectations() {{
            allowing(ctx).introspector();
            will(returnValue(new Introspector(test)));
            one(ctx).setAttribute(with("org.jmock.Mockery"), with(any(Mockery.class)));
        }});
        assertNull(test.get());
        plugin.prepareTestInstance(ctx);
        assertNotNull(test.get());
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_provider_method_null() throws Exception {
        final ProviderTest test = new ProviderTest() {
            @MockContextProvider
            private Mockery build() {
                return null;
            }
        };
        mockery.checking(new Expectations() {{
            allowing(ctx).introspector();
            will(returnValue(new Introspector(test)));
        }});
        assertNull(test.get());
        plugin.prepareTestInstance(ctx);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_provider_method_bad() throws Exception {
        final ProviderTest test = new ProviderTest() {
            @MockContextProvider
            private String build() {
                return "a string";
            }
        };
        mockery.checking(new Expectations() {{
            allowing(ctx).introspector();
            will(returnValue(new Introspector(test)));
        }});
        assertNull(test.get());
        plugin.prepareTestInstance(ctx);
    }

    @Test
    public void test_provider_method_before_field() throws Exception {
        final ProviderTest test = new ProviderTest() {
            @MockContextProvider
            private Mockery m = new Mockery();

            @MockContextProvider
            private Mockery build() {
                return JMock2TestPluginTest.this.mockery3;
            }
        };
        mockery.checking(new Expectations() {{
            allowing(ctx).introspector();
            will(returnValue(new Introspector(test)));
            one(ctx).setAttribute(with("org.jmock.Mockery"), with(any(Mockery.class)));
        }});
        assertNull(test.get());
        plugin.prepareTestInstance(ctx);
        assertNotNull(test.get());
        assertEquals(test.get(), mockery3);
    }
}
