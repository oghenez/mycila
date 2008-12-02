package com.mycila.testing.plugin.mockito;

import org.mockito.MockitoAnnotations;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class TestBase {

    @MockitoAnnotations.Mock
    Service service;

}
