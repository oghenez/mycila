package com.mycila.testing;

import com.mycila.testing.core.DefaultTestManager;
import com.mycila.testing.core.TestManager;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class Testing {

    private static final Testing instance = new Testing();

    private final TestManager manager;

    public Testing() {
        
        manager = new DefaultTestManager();
    }

    public static void excludeLoading(String... plugins) {
        
    }

    public static void prepare(Object testInstance) {
        instance.manager.prepare(testInstance);
    }

}
