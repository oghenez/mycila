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

package com.mycila.plugin.spi.invoke;

import com.mycila.plugin.spi.aop.CustomClassLoader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(JUnit4.class)
public final class InvokablesTest {
    public String test;

    @Test
    public void test() throws Exception {
        assertTrue(InvokableFastCtor.class.isInstance(Invokables.get(getClass().getConstructor())));
        assertTrue(InvokableFastMethod.class.isInstance(Invokables.get(getClass().getMethod("test"), this)));
        assertTrue(InvokableField.class.isInstance(Invokables.get(getClass().getField("test"), this)));
    }

    @Test
    public void test_bridge() throws Exception {
        CustomClassLoader loader = CustomClassLoader.isolated()
                .add(new File("target/classes"))
                .add(new File("target/test-classes"));

        Object dummy = loader.load(Funny.class.getName()).newInstance();
        Invokable invokable = Invokables.get(dummy.getClass().getConstructor());
        assertTrue(InvokableFastCtor.class.isInstance(invokable));
        assertTrue(InvokableFastMethod.class.isInstance(Invokables.get(dummy.getClass().getMethod("bear"), dummy)));
    }

    @Test
    public void test_without_aop() throws Exception {
        CustomClassLoader loader = CustomClassLoader.isolated()
                .add(new File("target/classes"))
                .add(new File("target/test-classes"));
        Class<?> invokablesClass = loader.load(Invokables.class.getName());
        Method get = invokablesClass.getMethod("get", Constructor.class);
        Object invokable = get.invoke(null, loader.load(Funny.class.getName()).getConstructor());
        Class<?> invokableClass = loader.load(Invokable.class.getName());
        assertTrue(invokableClass.isInstance(invokable));
        Object funny = invokableClass.getMethod("invoke", Object[].class).invoke(invokable, new Object[]{new Object[0]});
        assertEquals("abcd", funny.toString());
    }

    public static class Funny {
        public void bear() {
        }

        @Override
        public String toString() {
            return "abcd";
        }
    }
}
