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
package com.mycila.testing.core.introspect;

import com.mycila.testing.MycilaTestingException;
import com.mycila.testing.core.MyPlugin;
import static com.mycila.testing.core.introspect.Filters.*;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class IntrospectorTest {

    String field1;

    public int field2;

    @Annot
    private int field3;

    @Test
    public void test_getTarget() throws Exception {
        Introspector ti = new Introspector(this);
        assertEquals(ti.instance(), this);
    }

    @Test
    public void test_getTargetClass() throws Exception {
        Introspector ti = new Introspector(this);
        assertEquals(ti.testClass(), getClass());
    }

    @Test
    public void test_getFieldsOfType() throws Exception {
        Introspector ti = new Introspector(this);
        assertEquals(ti.selectFields(fieldsOfType(int.class)).size(), 2);
        assertEquals(ti.selectFields(fieldsOfType(int.class)).get(0).getName(), "field2");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_getFieldsOfType_null() throws Exception {
        Introspector ti = new Introspector(this);
        ti.selectFields(null);
    }

    @Test
    public void test_getFieldsOfTypeAnnotatedWith() throws Exception {
        Introspector ti = new Introspector(this);
        assertEquals(ti.selectFields(and(fieldsOfType(int.class), fieldsAnnotatedBy(Annot.class))).size(), 1);
        assertEquals(ti.selectFields(and(fieldsOfType(int.class), fieldsAnnotatedBy(Annot.class))).get(0).getName(), "field3");
    }

    @Test
    public void test_getFieldsAnnotatedWith() throws Exception {
        Introspector ti = new Introspector(this);
        assertEquals(ti.selectFields(fieldsAnnotatedBy(Annot.class)).size(), 1);
        assertEquals(ti.selectFields(fieldsAnnotatedBy(Annot.class)).get(0).getName(), "field3");
    }

    @Test
    public void test_getFieldsAnnotatedWith_accessibility() throws Exception {
        Introspector ti = new Introspector(new MyPlugin());
        assertEquals(ti.selectFields(fieldsAnnotatedBy(Annot.class)).size(), 1);
    }

    @Test
    public void test_getMethodsOfType() throws Exception {
        Introspector ti = new Introspector(this);
        assertEquals(ti.selectMethods(methodsOfType(int.class)).size(), 1);
    }

    @Test
    public void test_getMethodsOfType2() throws Exception {
        Introspector ti = new Introspector(this);
        List<Method> m = ti.selectMethods(methodsOfType(String.class));
        for (Method method : m) {
            System.out.println("- " + method.getName());
        }
        assertEquals(m.size(), 1);
    }

    @Test
    public void test_getMethodsOfType3() throws Exception {
        Introspector ti = new Introspector(this);
        List<Method> m = ti.selectMethods(methodsOfType(CharSequence.class));
        for (Method method : m) {
            System.out.println("- " + method.getName());
        }
        assertEquals(m.size(), 1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_getMethodsOfType_null() throws Exception {
        Introspector ti = new Introspector(this);
        ti.selectMethods(methodsOfType(null));
    }

    @Test
    public void test_getMethodsAnnotatedWith_inherited() throws Exception {
        class A {
            @Annot
            private void method1() {
            }

            @Annot
            void method2() {
            }
        }
        class B extends A {
            @Annot
            private void method1() {
            }

            @Annot
            void method2() {
            }
        }
        Introspector ti = new Introspector(new B());
        assertEquals(ti.selectMethods(methodsAnnotatedBy(Annot.class)).size(), 3);
    }

    @Test
    public void test_getMethodsAnnotatedWith() throws Exception {
        Introspector ti = new Introspector(this);
        assertEquals(ti.selectMethods(methodsAnnotatedBy(Test.class)).size(), 19);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_getMethodsAnnotatedWith_null() throws Exception {
        Introspector ti = new Introspector(this);
        ti.selectMethods(methodsAnnotatedBy(null));
    }

    @Test
    public void test_getMethodsOfTypeAnnotatedWith() throws Exception {
        Introspector ti = new Introspector(this);
        assertEquals(ti.selectMethods(and(methodsOfType(int.class), methodsAnnotatedBy(Annot.class))).size(), 1);
    }

    @Test
    public void test_invoke() throws Exception {
        Introspector ti = new Introspector(this);
        assertEquals(ti.selectMethods(and(methodsOfType(String.class), methodsAnnotatedBy(Annot.class))).size(), 1);
        assertEquals(ti.invoke(ti.selectMethods(and(methodsOfType(String.class), methodsAnnotatedBy(Annot.class))).get(0)), "10");
    }

    @Test(expectedExceptions = MycilaTestingException.class)
    public void test_invoke_exc() throws Exception {
        Introspector ti = new Introspector(this);
        assertEquals(ti.selectMethods(and(methodsOfType(int.class), methodsAnnotatedBy(Annot.class))).size(), 1);
        ti.invoke(ti.selectMethods(and(methodsOfType(int.class), methodsAnnotatedBy(Annot.class))).get(0));
    }

    @Test(expectedExceptions = MycilaTestingException.class)
    public void test_invoke_bad_args() throws Exception {
        Introspector ti = new Introspector(this);
        assertEquals(ti.selectMethods(and(methodsOfType(String.class), methodsAnnotatedBy(Annot.class))).size(), 1);
        ti.invoke(ti.selectMethods(and(methodsOfType(String.class), methodsAnnotatedBy(Annot.class))).get(0), 10, 20);
    }

    @Test
    public void test_get_set() throws Exception {
        Introspector ti = new Introspector(new MyPlugin());
        assertNull(ti.get(ti.selectFields(fieldsAnnotatedBy(Annot.class)).get(0)));
        ti.set(ti.selectFields(fieldsAnnotatedBy(Annot.class)).get(0), 10);
        assertEquals(ti.get(ti.selectFields(fieldsAnnotatedBy(Annot.class)).get(0)), 10);
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
    @Inherited
    public static @interface Annot {
    }
}
