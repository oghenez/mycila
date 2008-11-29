package com.mycila.testing.plugin.jmock;

import com.mycila.testing.core.TestSetup;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Test4 extends Test3 {
    @Mock
    private TestSetup ti;

    public TestSetup getTi4() {
        return ti;
    }
}