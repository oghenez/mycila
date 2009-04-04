package com.mycila.testing.core;

import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface TestHandler {
    void beforeTest(Method method);
    void afterTest(Method method, Throwable throwable);
}
