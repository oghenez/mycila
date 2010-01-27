package com.mycila.ujd.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface Analyzer {
    JVM getJVM();
    void close();
}
