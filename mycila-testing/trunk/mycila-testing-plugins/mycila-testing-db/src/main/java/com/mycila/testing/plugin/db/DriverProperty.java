package com.mycila.testing.plugin.db;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public @interface DriverProperty {
    public abstract String name();

    public abstract String value();
}
