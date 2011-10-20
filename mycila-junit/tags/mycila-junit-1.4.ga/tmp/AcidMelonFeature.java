package com.ovea.acidmelon.agent.testing;

import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public interface AcidMelonFeature {

    void init(Class<?> testClass) throws Throwable;

    void beforeClass(Class<?> testClass) throws Throwable;

    void afterClass(Class<?> testClass) throws Throwable;

    void init(Object testInstance, Method method) throws Throwable;

    void before(Object testInstance, Method method) throws Throwable;

    void after(Object testInstance, Method method) throws Throwable;
}
