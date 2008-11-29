package com.mycila.testing.core;

import com.mycila.testing.MyPlugin;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class TestInstanceTest {

    String field1;

    public int field2;

    @Annot
    private int field3;

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
    public void test_getFieldsOfType_null() throws Exception {
        TestInstance ti = new TestInstance(this);
        assertEquals(ti.getFieldsOfType(null).length, 0);
    }

    @Test
    public void test_getFieldsAnnotatedWith() throws Exception {
        TestInstance ti = new TestInstance(this);
        assertEquals(ti.getFieldsAnnotatedWith(Annot.class).length, 1);
        assertEquals(ti.getFieldsAnnotatedWith(Annot.class)[0].getName(), "field3");
    }

    @Test
    public void test_getFieldsAnnotatedWith_accessibility() throws Exception {
        TestInstance ti = new TestInstance(new MyPlugin());
        assertEquals(ti.getFieldsAnnotatedWith(Annot.class).length, 1);
    }

    @Test
    public void test_getFieldsAnnotatedWith_null() throws Exception {
        TestInstance ti = new TestInstance(this);
        assertEquals(ti.getFieldsAnnotatedWith(null).length, 0);
    }

    @Test
    public void test_getMethodsAnnotatedWith() throws Exception {
        TestInstance ti = new TestInstance(this);
        assertEquals(ti.getMethodsAnnotatedWith(Test.class).length, 12);
    }

    @Test
    public void test_getMethodsAnnotatedWith_null() throws Exception {
        TestInstance ti = new TestInstance(this);
        assertEquals(ti.getMethodsAnnotatedWith(null).length, 0);
    }

    @Test
    public void test_invoke() throws Exception {
        TestInstance ti = new TestInstance(this);
        assertEquals(ti.getMethodsAnnotatedWith(Annot.class).length, 2);
        assertEquals(ti.invoke(ti.getMethodsAnnotatedWith(Annot.class)[0]), 10);
    }

    @Test(expectedExceptions = TestPluginException.class)
    public void test_invoke_exc() throws Exception {
        TestInstance ti = new TestInstance(this);
        assertEquals(ti.getMethodsAnnotatedWith(Annot.class).length, 2);
        ti.invoke(ti.getMethodsAnnotatedWith(Annot.class)[1]);
    }

    @Test
    public void test_get_set() throws Exception {
        TestInstance ti = new TestInstance(new MyPlugin());
        assertNull(ti.get(ti.getFieldsAnnotatedWith(Annot.class)[0]));
        ti.set(ti.getFieldsAnnotatedWith(Annot.class)[0], 10);
        assertEquals(ti.get(ti.getFieldsAnnotatedWith(Annot.class)[0]), 10);
    }

    @Annot
    private int method() throws Exception {
        return 10;
    }

    @Annot
    private int method2() throws Exception {
        throw new IllegalArgumentException("error !");
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.METHOD})
    public static @interface Annot {
    }
}
