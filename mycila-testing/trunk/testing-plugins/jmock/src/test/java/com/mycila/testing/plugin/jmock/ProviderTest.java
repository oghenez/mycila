package com.mycila.testing.plugin.jmock;

import org.jmock.Mockery;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class ProviderTest {
    @MockContext
    private Mockery ctx;

    Mockery get() {
        return ctx;
    }
}
