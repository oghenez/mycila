package com.mycila.plugin.spi;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
abstract class MyAbstractPlugin implements MyPlugin {

    @Override
    public String toString() {
        return getClass().getSimpleName() + ": " + getBefore() + " | " + getAfter();
    }
}
