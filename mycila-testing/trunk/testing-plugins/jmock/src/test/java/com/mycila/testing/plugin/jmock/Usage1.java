package com.mycila.testing.plugin.jmock;

import com.mycila.testing.core.TestSetup;
import org.jmock.Expectations;
import org.jmock.Mockery;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Usage1 {

    @MockContext
    Mockery mockery;

    @Mock
    Service service;

    @BeforeClass
    public void setup() {
        TestSetup.setup(this);
    }

    @Test
    public void test_go() {
        mockery.checking(new Expectations() {{
            one(service).go();
            will(returnValue("Hello world !"));
        }});
        assertEquals(service.go(), "Hello world !");
        mockery.assertIsSatisfied();
    }
}
