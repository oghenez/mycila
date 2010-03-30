package com.mycila.jmx.export;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public enum Role {
    OPERATION,
    GETTER,
    SETTER;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
