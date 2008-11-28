package com.mycila.testing.core;

import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public abstract class AbstractTestPlugin implements TestPlugin {
    public List<String> getBefore() {
        return null;
    }

    public List<String> getAfter() {
        return null;
    }
}
