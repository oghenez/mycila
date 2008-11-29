package com.mycila.testing.plugin.jmock;

import com.mycila.testing.core.TestSetup;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
class Test3 {
    @Mock
    private TestSetup ti;

    public TestSetup getTi3() {
        return ti;
    }
}