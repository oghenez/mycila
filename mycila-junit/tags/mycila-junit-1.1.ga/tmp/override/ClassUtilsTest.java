package com.mycila.sandbox.override;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class ClassUtilsTest {

    @Test
    public void test_getAllDeclaredMethods() throws Exception {
        for (Method method : ClassUtils.getAllDeclaredMethods(getClass()))
            System.out.println(method);
        for (Method method : ClassUtils.getAllDeclaredMethods(B.class))
            System.out.println(method);
    }

    interface A {
        String method1();
        String method2();
    }

    interface B extends A {
        @Override
        String method1();
        String method1(int a);
        String method3();
    }
}
