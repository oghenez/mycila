package com.mycila.testing.plugin.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.mycila.testing.testng.AbstractMycilaTestNGTest;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@GuiceContext(OverrideTest.MyModule.class)
public final class OverrideTest extends AbstractMycilaTestNGTest {

    @Inject
    String value;

    @Bind
    String a = "B";

    @Test
    public void test_overide() throws Exception {
        assertEquals(value, "B");
    }

    static final class MyModule extends AbstractModule {
        protected void configure() {
            bind(String.class).toInstance("A");
        }
    }
}
