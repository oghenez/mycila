/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mycila.testing.core;

import com.mycila.testing.MyPlugin;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

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

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_getFieldsOfType_null() throws Exception {
        TestInstance ti = new TestInstance(this);
        ti.getFieldsOfType(null);
    }

    @Test
    public void test_getFieldsOfTypeAnnotatedWith() throws Exception {
        TestInstance ti = new TestInstance(this);
        assertEquals(ti.getFieldsOfTypeAnnotatedWith(int.class, Annot.class).length, 1);
        assertEquals(ti.getFieldsOfTypeAnnotatedWith(int.class, Annot.class)[0].getName(), "field3");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_getFieldsOfTypeAnnotatedWith_null1() throws Exception {
        TestInstance ti = new TestInstance(this);
        ti.getFieldsOfTypeAnnotatedWith(null, Annot.class);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_getFieldsOfTypeAnnotatedWith_null2() throws Exception {
        TestInstance ti = new TestInstance(this);
        ti.getFieldsOfTypeAnnotatedWith(MyPlugin.class, null);
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

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_getFieldsAnnotatedWith_null() throws Exception {
        TestInstance ti = new TestInstance(this);
        ti.getFieldsAnnotatedWith(null);
    }

    @Test
    public void test_getMethodsOfType() throws Exception {
        TestInstance ti = new TestInstance(this);
        assertEquals(ti.getMethodsOfType(int.class).length, 2); // also Object.hashCode()
    }

    @Test
    public void test_getMethodsOfType2() throws Exception {
        TestInstance ti = new TestInstance(this);
        Method[] m = ti.getMethodsOfType(String.class);
        for (Method method : m) {
            System.out.println("- " + method.getName());
        }
        assertEquals(m.length, 2);
    }

    @Test
    public void test_getMethodsOfType3() throws Exception {
        TestInstance ti = new TestInstance(this);
        Method[] m = ti.getMethodsOfType(CharSequence.class);
        for (Method method : m) {
            System.out.println("- " + method.getName());
        }
        assertEquals(m.length, 2);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_getMethodsOfType_null() throws Exception {
        TestInstance ti = new TestInstance(this);
        ti.getMethodsOfType(null);
    }

    @Test
    public void test_getMethodsAnnotatedWith() throws Exception {
        TestInstance ti = new TestInstance(this);
        assertEquals(ti.getMethodsAnnotatedWith(Test.class).length, 21);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_getMethodsAnnotatedWith_null() throws Exception {
        TestInstance ti = new TestInstance(this);
        ti.getMethodsAnnotatedWith(null);
    }

    @Test
    public void test_getMethodsOfTypeAnnotatedWith() throws Exception {
        TestInstance ti = new TestInstance(this);
        assertEquals(ti.getMethodsOfTypeAnnotatedWith(int.class, Annot.class).length, 1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_getMethodsOfTypeAnnotatedWith_null1() throws Exception {
        TestInstance ti = new TestInstance(this);
        ti.getMethodsOfTypeAnnotatedWith(null, Annot.class);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_getMethodsOfTypeAnnotatedWith_null2() throws Exception {
        TestInstance ti = new TestInstance(this);
        ti.getMethodsOfTypeAnnotatedWith(int.class, null);
    }

    @Test
    public void test_invoke() throws Exception {
        TestInstance ti = new TestInstance(this);
        assertEquals(ti.getMethodsOfTypeAnnotatedWith(String.class, Annot.class).length, 1);
        assertEquals(ti.invoke(ti.getMethodsOfTypeAnnotatedWith(String.class, Annot.class)[0]), "10");
    }

    @Test(expectedExceptions = TestPluginException.class)
    public void test_invoke_exc() throws Exception {
        TestInstance ti = new TestInstance(this);
        assertEquals(ti.getMethodsOfTypeAnnotatedWith(int.class, Annot.class).length, 1);
        ti.invoke(ti.getMethodsOfTypeAnnotatedWith(int.class, Annot.class)[0]);
    }

    @Test(expectedExceptions = TestPluginException.class)
    public void test_invoke_bad_args() throws Exception {
        TestInstance ti = new TestInstance(this);
        assertEquals(ti.getMethodsOfTypeAnnotatedWith(String.class, Annot.class).length, 1);
        ti.invoke(ti.getMethodsOfTypeAnnotatedWith(String.class, Annot.class)[0], 10, 20);
    }

    @Test
    public void test_get_set() throws Exception {
        TestInstance ti = new TestInstance(new MyPlugin());
        assertNull(ti.get(ti.getFieldsAnnotatedWith(Annot.class)[0]));
        ti.set(ti.getFieldsAnnotatedWith(Annot.class)[0], 10);
        assertEquals(ti.get(ti.getFieldsAnnotatedWith(Annot.class)[0]), 10);
    }

    @Annot
    private String method() throws Exception {
        return "10";
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
