package com.mycila.guice;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class MethodsTest {

    @Test
    public void test_if() throws Exception {
        for (Method method : Methods.listAll(B.class))
            System.out.println(method);
        assertEquals(3, Methods.listAll(B.class).size());
    }

    @Test
    public void test_class() throws Exception {
        for (Method method : Methods.listAll(BB.class))
            System.out.println(method);
        assertEquals(5, Methods.listAll(BB.class).size());
    }

    private static interface A {
        void test1();

        void test2();
    }

    private static interface B extends A {
        void test1();

        void test();
    }

    private static class AA {
        private void test1(){}
        void test2(){}
        void test3(){}
    }

    private static class BB extends AA{
        private void test1(){}
        void test2(){}
        void test4(){}
    }
}
