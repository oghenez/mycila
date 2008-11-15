package com.mycila.testing.junit;

import com.mycila.testing.core.TestSetup;
import junit.framework.TestCase;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class AbstractMycilaJunitTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        TestSetup.set(this);
    }
    
}
