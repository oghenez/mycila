package com.mycila.testing.junit;

import com.mycila.testing.core.TestSetup;
import org.junit.BeforeClass;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class AbstractMycilaJunit4Test {

    @BeforeClass
    protected void setup() {
        TestSetup.set(this);
    }

}