package com.mycila.testing.plugin;

import com.mycila.testing.core.TestSetup;
import org.mockito.MockitoAnnotations;
import static org.testng.Assert.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public class InjectTest extends TestBase {

    @MockitoAnnotations.Mock
    Service service;

    @BeforeClass
    public void setup() {
        TestSetup.setup(this);
    }

    @Test
    public void test_mock_standard() {
        assertNotNull(service);
        assertNotNull(super.service);
    }
}