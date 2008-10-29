package com.mycila.testing.core;

import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Context {

    TestPreparator getCurrentPreparator();

    List<TestPreparator> getAllPreparators();

    List<TestPreparator> getExecutedPreparators();

    List<TestPreparator> getPendingPreparators();

    TestPreparator getPreparator(String pluginName);

    Object getTestInstance();

}
