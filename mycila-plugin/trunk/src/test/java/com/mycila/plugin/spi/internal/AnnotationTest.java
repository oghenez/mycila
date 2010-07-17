package com.mycila.plugin.spi.internal;

import com.mycila.plugin.test.CurrentMethod;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class AnnotationTest {

    @Rule
    public final CurrentMethod currentMethod = new CurrentMethod();

    @Test
    public void test_simple_annot() throws Exception {
        Test test = AnnotationUtils.buildRandomAnnotation(Test.class);
        Test test2 = AnnotationUtils.buildRandomAnnotation(Test.class);
        assertTrue(test.equals(test2));
        assertTrue(currentMethod.get().getAnnotation(Test.class).equals(test));
        assertTrue(test.equals(currentMethod.get().getAnnotation(Test.class)));
        assertTrue(currentMethod.get().getAnnotation(Test.class).hashCode() == test.hashCode());
    }

    @Test
    public void test_fail() throws Exception {
        try {
            AnnotationUtils.buildRandomAnnotation(Failing.class);
            fail();
        } catch (Exception e) {
            assertEquals(UnsupportedOperationException.class, e.getClass());
        }
    }

    @Test
    @Annot(value = Void.class, test = "", unit = TimeUnit.NANOSECONDS)
    public void test_complex() throws Exception {
        Annot test = AnnotationUtils.buildRandomAnnotation(Annot.class);
        assertEquals(Void.class, test.value());
        assertTrue(currentMethod.get().getAnnotation(Annot.class).equals(test));
        assertTrue(test.equals(currentMethod.get().getAnnotation(Annot.class)));
        assertTrue(currentMethod.get().getAnnotation(Annot.class).hashCode() == test.hashCode());
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.PARAMETER})
    static public @interface Annot {
        int def() default 5;

        Class value();

        String test();

        TimeUnit unit();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.PARAMETER})
    static public @interface Failing {
        Zero ZERO();
    }

    private static enum Zero{}
}
