package com.mycila.testing.core;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface TestPlugin {

    String[] getDependencies();

    TestPreparator getPreparator(Object testInstance);

}
