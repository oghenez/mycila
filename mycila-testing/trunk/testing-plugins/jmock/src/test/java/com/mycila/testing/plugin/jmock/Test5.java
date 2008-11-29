package com.mycila.testing.plugin.jmock;

import org.hamcrest.SelfDescribing;
import org.jmock.Mockery;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */

final class Test5 {
    @MockContext
    Mockery mockery1 = new Mockery();

    @MockContext
    SelfDescribing mockery2;
}