package com.mycila.event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Method;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class SignatureTest {

    @Test
    public void test() throws Exception {
        for (Method method : DefaultDispatcher.class.getDeclaredMethods())
            System.out.println(MethodSignature.of(method));
        for (Method method : ErrorHandlers.class.getDeclaredMethods())
            System.out.println(MethodSignature.of(method));
        for (Method method : SignatureTest.class.getDeclaredMethods())
            System.out.println(MethodSignature.of(method));
    }

    @Test
    public void test2() throws Exception {
        for (Method method : AnnotationProcessor.getAllDeclaredMethods(SignatureTest.class))
            System.out.println(MethodSignature.of(method));
        for (Method method : AnnotationProcessor.getAllDeclaredMethods(MethodSignature.class))
            System.out.println(MethodSignature.of(method));
        for (Method method : AnnotationProcessor.getAllDeclaredMethods(ErrorHandlers.class))
            System.out.println(MethodSignature.of(method));
        for (Method method : AnnotationProcessor.getAllDeclaredMethods(DefaultDispatcher.class))
            System.out.println(MethodSignature.of(method));
        /*for (Method method : AnnotationProcessor.getAllDeclaredMethods(JButton.class))
            System.out.println(MethodSignature.of(method));*/
    }

    static void test_array(Class<?>[]... a) {
    }

    static void test_int(int a) {
    }

    static void test_int(int[][] a) {
    }
}
