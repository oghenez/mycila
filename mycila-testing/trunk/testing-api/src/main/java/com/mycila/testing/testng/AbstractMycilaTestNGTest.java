package com.mycila.testing.testng;

import com.mycila.testing.core.TestSetup;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class AbstractMycilaTestNGTest extends Assert {

    @BeforeClass
    public void setup() {
        TestSetup.set(this);
    }
    
}