/**
 * Copyright (C) 2010 Mathieu Carbou <mathieu.carbou@gmail.com>
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

package com.mycila.plugin.spi.model;

import com.mycila.plugin.annotation.BindingAnnotation;
import com.mycila.plugin.spi.invoke.Invokables;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.swing.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class BindingTest {

    @Test
    public void test_get_annot() throws Exception {
        Method m2 = getClass().getMethod("annotated2");
        Binding binding = Binding.get(JButton.class, Personal.class);
        System.out.println(binding);
        System.out.println(m2.getAnnotation(Personal.class));
        assertEquals(m2.getAnnotation(Personal.class), binding.getAnnotations().iterator().next());
    }

    @Test
    public void test_get_annot_complex() throws Exception {
        Method m1 = getClass().getMethod("annotated", String.class, Integer.class);
        Binding binding = Binding.get(JButton.class, Internal.class);
        System.out.println(binding);
        System.out.println(m1.getAnnotation(Internal.class));
        assertEquals(binding.getAnnotations().iterator().next(), m1.getAnnotation(Internal.class));
        assertEquals(m1.getAnnotation(Internal.class), binding.getAnnotations().iterator().next());
    }

    @Test
    public void test_invokables() throws Exception {
        Method m1 = getClass().getMethod("annotated", String.class, Integer.class);
        Binding binding = Binding.fromInvokable(Invokables.<Object>get(m1, this));
        System.out.println(binding);
        assertEquals(2, binding.getAnnotations().size());
    }

    @Test
    public void test_params() throws Exception {
        Method m1 = getClass().getMethod("annotated", String.class, Integer.class);
        List<Binding<?>> bindings = Binding.fromParameters(m1);
        System.out.println(bindings);
        assertEquals(2, bindings.size());
    }

    @Test
    public void test_equals() throws Exception {
        Binding<?> binding1 = Binding.get(String.class, Internal.class, Personal.class);
        Binding<?> binding2 = Binding.get(String.class, Personal.class, Internal.class);
        assertTrue(binding1.equals(binding2));
    }

    @Personal
    @Internal(test = "", value = 5)
    public void annotated(@Personal String param, @Internal(test = "456") Integer count) {

    }

    @Personal
    public void annotated2() {

    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
    @BindingAnnotation
    static public @interface Personal {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD})
    @BindingAnnotation
    static public @interface Internal {
        int value() default 5;

        String test();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD, ElementType.PARAMETER})
    @BindingAnnotation
    static public @interface Internal2 {
        Class value();

        String test();

        TimeUnit unit();
    }

}
