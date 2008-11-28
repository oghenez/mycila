package com.mycila.testing.core;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class TestTest {

    String field1;

    int field2;

    @Annot
    int field3;

    @Test
    public void test_getTarget() throws Exception {
        TestInstance ti = new TestInstance(this);
        assertEquals(ti.getTarget(), this);
    }

    @Test
    public void test_getTargetClass() throws Exception {
        TestInstance ti = new TestInstance(this);
        assertEquals(ti.getTargetClass(), getClass());
    }

    @Test
    public void test_getFieldsOfType() throws Exception {
        TestInstance ti = new TestInstance(this);
        assertEquals(ti.getFieldsOfType(int.class).length, 2);
        assertEquals(ti.getFieldsOfType(int.class)[0].getName(), "field2");
    }

    @Test
    public void test_getFieldsAnnotatedWith() throws Exception {
        TestInstance ti = new TestInstance(this);
        assertEquals(ti.getFieldsAnnotatedWith(Annot.class).length, 1);
        assertEquals(ti.getFieldsAnnotatedWith(Annot.class)[0].getName(), "field3");
    }

    @Test
    public void test_getMethodsAnnotatedWith() throws Exception {
        TestInstance ti = new TestInstance(this);
        assertEquals(ti.getMethodsAnnotatedWith(Test.class).length, 8);
    }

    @Test
    public void test_invoke() throws Exception {
    }

    @Test
    public void test_get() throws Exception {
    }

    @Test
    public void test_set() throws Exception {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD})
    static @interface Annot {
    }
}
