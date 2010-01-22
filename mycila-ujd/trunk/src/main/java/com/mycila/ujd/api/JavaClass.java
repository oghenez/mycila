package com.mycila.ujd.api;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface JavaClass<T> {
    String getClassName();
    Class<T> get();
}