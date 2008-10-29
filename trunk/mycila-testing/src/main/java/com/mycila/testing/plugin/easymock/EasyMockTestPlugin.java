package com.mycila.testing.plugin.easymock;

import com.mycila.testing.core.TestPlugin;
import com.mycila.testing.core.TestPreparator;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class EasyMockTestPlugin implements TestPlugin {
    public String[] getDependencies() {
        return new String[0];
    }

    public TestPreparator getPreparator(Object testInstance) {
        return null;
    }
}