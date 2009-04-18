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
import java.lang.reflect.Field;
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
    public void test_all() throws Exception {
        Introspector ti = new Introspector(this);
        assertEquals(ti.instance(), this);
    }

    @Test
    public void test_filter_all() throws Exception {
        Introspector ti = new Introspector(this);
        System.out.println(ti.selectFields(Filters.<Field>all()));
        assertEquals(ti.selectFields(Filters.<Field>all()).size(), 3);
    }

    @Test
    public void test_filter_none() throws Exception {
        Introspector ti = new Introspector(this);
        System.out.println(ti.selectFields(Filters.<Field>all()));
        assertEquals(ti.selectFields(Filters.<Field>none()).size(), 0);
    }

    @Test
    public void test_filter_not() throws Exception {
        Introspector ti = new Introspector(this);
        System.out.println(ti.selectFields(not(fieldsOfType(int.class))));
        assertEquals(ti.selectFields(not(fieldsOfType(int.class))).size(), 1);
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
        assertEquals(ti.selectMethods(methodsReturning(int.class)).size(), 1);
    }

    @Test
    public void test_getMethodsOfType2() throws Exception {
        Introspector ti = new Introspector(this);
        List<Method> m = ti.selectMethods(methodsReturning(String.class));
        for (Method method : m) {
            System.out.println("- " + method.getName());
        }
        assertEquals(m.size(), 1);
    }

    @Test
    public void test_getMethodsOfType3() throws Exception {
        Introspector ti = new Introspector(this);
        List<Method> m = ti.selectMethods(methodsReturning(CharSequence.class));
        for (Method method : m) {
            System.out.println("- " + method.getName());
        }
        assertEquals(m.size(), 1);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_getMethodsOfType_null() throws Exception {
        Introspector ti = new Introspector(this);
        ti.selectMethods(methodsReturning(null));
    }

    static interface I {
        void method3();
    }

    @Test
    public void test_getMethodsAnnotatedWith_inherited() throws Exception {
        abstract class A implements I {
            private void method1() {
            }

            void method2() {
            }

            protected abstract void method4();
        }
        class B extends A {
            private void method1() {
            }

            CharSequence method2(String s) {
                return null;
            }

            public void method3() {
            }

            @Override
            protected final void method4() {
            }
        }
        class C extends B {
            @Override
            String method2(String s) {
                return null;
            }

            @Override
            public void method3() {
            }
        }
        Introspector ti = new Introspector(new C());
        System.out.println("selected:");
        for (Method method : ti.selectMethods(Filters.<Method>all())) {
            System.out.println("- " + method);
        }
        assertEquals(ti.selectMethods(Filters.<Method>all()).size(), 10); // a volatile method is created for B.method2
        System.out.println("selected:");
        for (Method method : ti.selectMethods(excludeOverridenMethods(Filters.<Method>all()))) {
            System.out.println("- " + method);
        }
        assertEquals(ti.selectMethods(excludeOverridenMethods(Filters.<Method>all())).size(), 6);
    }

    @Test
    public void test_getMethodsAnnotatedWith() throws Exception {
        Introspector ti = new Introspector(this);
        assertEquals(ti.selectMethods(methodsAnnotatedBy(Test.class)).size(), 22);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test_getMethodsAnnotatedWith_null() throws Exception {
        Introspector ti = new Introspector(this);
        ti.selectMethods(methodsAnnotatedBy(null));
    }

    @Test
    public void test_getMethodsOfTypeAnnotatedWith() throws Exception {
        Introspector ti = new Introspector(this);
        assertEquals(ti.selectMethods(and(methodsReturning(int.class), methodsAnnotatedBy(Annot.class))).size(), 1);
    }

    @Test
    public void test_invoke() throws Exception {
        Introspector ti = new Introspector(this);
        assertEquals(ti.selectMethods(and(methodsReturning(String.class), methodsAnnotatedBy(Annot.class))).size(), 1);
        assertEquals(ti.invoke(ti.selectMethods(and(methodsReturning(String.class), methodsAnnotatedBy(Annot.class))).get(0)), "10");
    }

    @Test(expectedExceptions = MycilaTestingException.class)
    public void test_invoke_exc() throws Exception {
        Introspector ti = new Introspector(this);
        assertEquals(ti.selectMethods(and(methodsReturning(int.class), methodsAnnotatedBy(Annot.class))).size(), 1);
        ti.invoke(ti.selectMethods(and(methodsReturning(int.class), methodsAnnotatedBy(Annot.class))).get(0));
    }

    @Test(expectedExceptions = MycilaTestingException.class)
    public void test_invoke_bad_args() throws Exception {
        Introspector ti = new Introspector(this);
        assertEquals(ti.selectMethods(and(methodsReturning(String.class), methodsAnnotatedBy(Annot.class))).size(), 1);
        ti.invoke(ti.selectMethods(and(methodsReturning(String.class), methodsAnnotatedBy(Annot.class))).get(0), 10, 20);
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
